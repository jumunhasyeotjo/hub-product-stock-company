package com.jumunhasyeo.product.application;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.product.application.command.*;
import com.jumunhasyeo.product.application.dto.ProductRes;
import com.jumunhasyeo.product.application.service.CompanyClient;
import com.jumunhasyeo.product.application.service.UserClient;
import com.jumunhasyeo.product.domain.entity.Product;
import com.jumunhasyeo.product.domain.repository.ProductRepository;
import com.jumunhasyeo.product.domain.vo.CompanyId;
import com.jumunhasyeo.product.domain.vo.Price;
import com.jumunhasyeo.product.domain.vo.ProductDescription;
import com.jumunhasyeo.product.domain.vo.ProductName;
import com.jumunhasyeo.product.presentation.dto.req.ProductSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.jumunhasyeo.product.fixtures.ProductFixture.getProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private UserClient userClient;

    @Mock
    private CompanyClient companyClient;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    /** 생성 **/
    @Test
    @DisplayName("상품 생성 정상 프로세스")
    void createProduct_SuccessProcess() {
        // given
        CreateProductCommand req = new CreateProductCommand("상품", 1000, "설명", 1L);
        UUID companyID = UUID.randomUUID();

        given(userClient.getOrganizationId(1L))
                .willReturn(Optional.of(companyID));
        given(companyClient.existsCompany(companyID))
                .willReturn(true);
        given(productRepository.existsByName(ProductName.of(req.name())))
                .willReturn(false);

        // when
        ProductRes response = productServiceImpl.createProduct(req);

        // then
        assertThat(response.name()).isEqualTo(req.name());
        assertThat(response.price()).isEqualTo(req.price());
        assertThat(response.description()).isEqualTo(req.description());
    }

    @Test
    @DisplayName("상품은 존재하는 업체에서만 생성할 수 있다.")
    void 존재하는_업체만_가능() {
        // given
        CreateProductCommand req = new CreateProductCommand("상품", 1000, "설명", 1L);
        UUID companyID = UUID.randomUUID();

        given(userClient.getOrganizationId(1L))
                .willReturn(Optional.of(companyID));
        given(companyClient.existsCompany(companyID))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> productServiceImpl.createProduct(req))
                .isInstanceOf(BusinessException.class);
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("상품 명은 중복되어 생성할 수 없다.")
    void createProduct_WhenDuplicateName_shouldTrowException() {
        // given
        CreateProductCommand req = new CreateProductCommand("상품", 1000, "설명", 1L);
        UUID companyId = UUID.randomUUID();

        given(userClient.getOrganizationId(1L))
                .willReturn(Optional.of(companyId));
        given(companyClient.existsCompany(companyId))
                .willReturn(true);
        given(productRepository.existsByName(ProductName.of(req.name())))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> productServiceImpl.createProduct(req))
                .isInstanceOf(BusinessException.class);
        verify(productRepository, never()).save(any());
    }

    /** 수정 **/
    @Test
    @DisplayName("상품 수정 정상 프로세스")
    void updateProduct_SuccessProcess() {
        // given
        Product product = getProduct();
        UpdateProductCommand req = new UpdateProductCommand(product.getId(), 1L, "수정", 5000, "설명 수정");

        given(productRepository.findById(product.getId()))
                .willReturn(Optional.of(product));
        given(userClient.getOrganizationId(1L))
                .willReturn(Optional.ofNullable(product.getCompanyId().getCompanyId()));
        given(productRepository.existsByNameAndIdNot(ProductName.of(req.name()), product.getId()))
                .willReturn(false);

        // when
        ProductRes response = productServiceImpl.updateProduct(req);

        // then
        assertThat(response.name()).isEqualTo(req.name());
        assertThat(response.price()).isEqualTo(req.price());
        assertThat(response.description()).isEqualTo(req.description());
    }

    @Test
    @DisplayName("존재하는 상품만 수정 가능하다")
    void updateProduct__shouldTrowException() {
        // given
        Product product = getProduct();
        UpdateProductCommand req = new UpdateProductCommand(product.getId(), 1L, "수정", 5000, "설명 수정");

        given(productRepository.findById(product.getId()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productServiceImpl.updateProduct(req))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("상품 명은 중복되어 수정할 수 없다.")
    void updateProduct_WhenDuplicateName_shouldTrowException() {
        // given
        Product product = getProduct();
        UpdateProductCommand req = new UpdateProductCommand(product.getId(), 1L, "수정", 5000, "설명 수정");

        given(productRepository.findById(product.getId()))
                .willReturn(Optional.of(product));
        given(userClient.getOrganizationId(1L))
                .willReturn(Optional.ofNullable(product.getCompanyId().getCompanyId()));
        given(productRepository.existsByNameAndIdNot(ProductName.of(req.name()), product.getId()))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> productServiceImpl.updateProduct(req))
                .isInstanceOf(BusinessException.class);
    }

    /** 삭제 **/
    @Test
    @DisplayName("상품 삭제 정상 프로세스 - MASTER")
    void deleteProduct_Master_SuccessProcess() {
        // given
        Product product = getProduct();
        DeleteProductCommand req = new DeleteProductCommand(product.getId(), 1L, "MASTER");

        given(productRepository.findById(product.getId()))
                .willReturn(Optional.of(product));

        // when
        productServiceImpl.deleteProduct(req);

        // then
        assertThat(product.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("상품 삭제 정상 프로세스 - COMPANY_MANAGER")
    void deleteProduct_HubManager_SuccessProcess() {
        // given
        Product product = getProduct();
        DeleteProductCommand req = new DeleteProductCommand(product.getId(), 1L, "COMPANY_MANAGER");

        given(productRepository.findById(product.getId()))
                .willReturn(Optional.of(product));
        given(userClient.getOrganizationId(1L))
                .willReturn(Optional.ofNullable(product.getCompanyId().getCompanyId()));

        // when
        productServiceImpl.deleteProduct(req);

        // then
        assertThat(product.getDeletedAt()).isNotNull();
    }

    /** 조회 **/
    @Test
    @DisplayName("상품 단건 조회 정상 프로세스")
    void getProduct_SuccessProcess() {
        // given
        Product product = getProduct();
        GetProductCommand req = new GetProductCommand(product.getId());

        given(productRepository.findById(product.getId()))
                .willReturn(Optional.of(product));

        // when
        ProductRes response = productServiceImpl.getProduct(req);

        // then
        assertThat(response.name()).isEqualTo(product.getName().getName());
        assertThat(response.price()).isEqualTo(product.getPrice().getPrice());
        assertThat(response.description()).isEqualTo(product.getDescription().getDescription());
    }


    @Test
    @DisplayName("상품 다건 조회 정상 프로세스")
    void searchProducts_SuccessProcess() {
        // given
        UUID companyId = UUID.randomUUID();
        String searchName = "테스트";
        Pageable pageable = PageRequest.of(0, 10);

        ProductSearchCondition condition = new ProductSearchCondition(
                companyId,
                searchName,
                1000,
                20000
        );

        SearchProductCommand req = new SearchProductCommand(condition, pageable);

        List<ProductRes> content = List.of(
                new ProductRes(UUID.randomUUID(), "상품 A", 1500, "설명 A"),
                new ProductRes(UUID.randomUUID(), "상품 B", 5000, "설명 B")
        );
        Page<ProductRes> expectedPage = new PageImpl<>(content, pageable, content.size());

        given(productRepository.searchProduct(req))
                .willReturn(expectedPage);

        // when
        Page<ProductRes> response = productServiceImpl.searchProduct(req);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getTotalElements()).isEqualTo(2);
        assertThat(response.getContent().get(0).name()).isEqualTo("상품 A");
    }

}
