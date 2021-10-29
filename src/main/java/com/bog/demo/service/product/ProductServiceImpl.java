package com.bog.demo.service.product;

import com.bog.demo.domain.file.File;
import com.bog.demo.domain.product.Product;
import com.bog.demo.domain.product.Sell;
import com.bog.demo.domain.user.User;
import com.bog.demo.facade.AuthenticationFacade;
import com.bog.demo.mapper.product.ProductMapper;
import com.bog.demo.model.product.BuyProductDto;
import com.bog.demo.model.product.ProductDto;
import com.bog.demo.model.product.ProductSearchRequestDto;
import com.bog.demo.model.product.ProductSearchResponseDto;
import com.bog.demo.model.user.UserDto;
import com.bog.demo.repository.product.ProductRepository;
import com.bog.demo.repository.sell.SellRepository;
import com.bog.demo.repository.user.UserRepository;
import com.bog.demo.util.Descriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class, readOnly = true)
public class ProductServiceImpl implements ProductService {

    @PersistenceContext
    private EntityManager em;

    private ProductMapper productMapper;

    private ProductRepository productRepository;

    private SellRepository sellRepository;

    private UserRepository userRepository;

    private AuthenticationFacade authenticationFacade;

    @Override
    public ProductSearchResponseDto searchProducts(ProductSearchRequestDto productSearchRequestDto) {
        StringBuilder select = new StringBuilder("FROM Product d where state=1 ");
        StringBuilder selectCount = new StringBuilder("FROM Product d where state=1 ");

        StringBuilder where = new StringBuilder("");
        Map<String, Object> queryParams = new HashMap<>();

        String name = productSearchRequestDto.getName();
        if (Objects.nonNull(name) && !name.trim().isEmpty()) {
            where.append("and d.name like :name ");
            queryParams.put("name", "%" + name + "%");
        }

        TypedQuery<Product> resultQuery = em.createQuery("SELECT d "
                + select
                + where
                + "ORDER BY d.name", Product.class);

        for (String param : queryParams.keySet()) {
            resultQuery.setParameter(param, queryParams.get(param));
        }

        TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(d.id) "
                + selectCount
                + where, Long.class);

        for (String param : queryParams.keySet()) {
            countQuery.setParameter(param, queryParams.get(param));
        }

        List<Product> result = resultQuery.getResultList();
        Long count = countQuery.getSingleResult();

        List<ProductDto> productDtos = result.stream().map((Product debt) -> productMapper.mapToDto(
                debt)
        ).collect(Collectors.toList());

        return new ProductSearchResponseDto(count, productDtos);
    }

    @Override
    @Transactional
    public Descriptor saveProduct(ProductDto productDto) {
        Product product = productMapper.mapToEntity(productDto);
        product.setState(1);
        product.setUserId(authenticationFacade.getUser().getId());
        productRepository.save(product);

        return Descriptor.validDescriptor();
    }

    @Override
    @Transactional
    public Descriptor updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).get();

        Integer userId = authenticationFacade.getUser().getId();
        if (!userId.equals(product.getUserId())) {
            return Descriptor.invalidDescriptor("NOT_PRODUCT_OWNER");
        }

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        if (productDto.getFileId() != null) {
            product.setFile(new File(productDto.getFileId()));
        } else {
            product.setFile(null);
        }

        productRepository.save(product);

        return Descriptor.validDescriptor();
    }

    @Override
    @Transactional
    public Descriptor buyProduct(BuyProductDto buyProductDto) {
        Product product = productRepository.findById(buyProductDto.getProductId()).get();
        if (product.getQuantity() < buyProductDto.getQuantity()) {
            return Descriptor.invalidDescriptor("NOT_ENOUGH_AMOUNT");
        }

        product.setQuantity(product.getQuantity() - buyProductDto.getQuantity());

        UserDto authUser = authenticationFacade.getUser();
        Integer userId = null;
        if (authUser != null) {
            userId = authUser.getId();
        }

        Sell sell = new Sell();
        sell.setProductId(buyProductDto.getProductId());
        sell.setQuantity(buyProductDto.getQuantity());
        sell.setOnePrice(product.getPrice());
        sell.setCost(buyProductDto.getQuantity() * product.getPrice());
        sell.setCardInfo(buyProductDto.getCardInfo());
        sell.setState(1);
        sell.setCreateDate(new Date());
        sell.setBuyerUserId(userId);
        sell.setSellerUserId(product.getUserId());

        User user = userRepository.findById(product.getUserId()).get();
        user.setBalance(user.getBalance() + sell.getCost() * 0.1);

        sellRepository.save(sell);
        productRepository.save(product);
        userRepository.save(user);

        return Descriptor.validDescriptor();
    }


    @Autowired
    public void setProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @Autowired
    public void setSellRepository(SellRepository sellRepository) {
        this.sellRepository = sellRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
