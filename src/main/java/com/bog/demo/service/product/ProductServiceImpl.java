package com.bog.demo.service.product;

import com.bog.demo.domain.file.File;
import com.bog.demo.domain.product.Product;
import com.bog.demo.mapper.product.ProductMapper;
import com.bog.demo.model.product.ProductDto;
import com.bog.demo.model.product.ProductSearchRequestDto;
import com.bog.demo.model.product.ProductSearchResponseDto;
import com.bog.demo.repository.product.ProductRepository;
import com.bog.demo.util.Descriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Throwable.class, readOnly = true)
public class ProductServiceImpl implements ProductService {

    @PersistenceContext
    private EntityManager em;

    private ProductMapper productMapper;

    private ProductRepository productRepository;

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
        productRepository.save(product);

        return Descriptor.validDescriptor();
    }

    @Override
    @Transactional
    public Descriptor updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).get();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setFile(new File(productDto.getFileId()));
        productRepository.save(product);

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
}
