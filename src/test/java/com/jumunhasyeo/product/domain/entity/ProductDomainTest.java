package com.jumunhasyeo.product.domain.entity;

import com.jumunhasyeo.product.domain.vo.CompanyId;
import com.jumunhasyeo.product.domain.vo.Price;
import com.jumunhasyeo.product.domain.vo.ProductDescription;
import com.jumunhasyeo.product.domain.vo.ProductName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.jumunhasyeo.product.fixtures.ProductFixture.getProduct;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDomainTest {

    /** 등록 **/
    @Test
    @DisplayName("상품 등록 정상 프로세스")
    void create_successProcess() {
        // given & when
        Product product = getProduct();

        // then
        assertThat(product.getName().getName()).isEqualTo("상품");
        assertThat(product.getDescription().getDescription()).isEqualTo("상품 설명");
        assertThat(product.getPrice().getPrice()).isEqualTo(10000);
    }

    /** 수정 **/
    @Test
    @DisplayName("상품 수정 정상 프로세스")
    void update_create_successProcess() {
        // given
        Product product = getProduct();

        // when
        product.update(ProductName.of("이름 수정"),
                ProductDescription.of("설명 수정"),
                Price.of(5000));

        // then
        assertThat(product.getName().getName()).isEqualTo("이름 수정");
        assertThat(product.getDescription().getDescription()).isEqualTo("설명 수정");
        assertThat(product.getPrice().getPrice()).isEqualTo(5000);
    }

    /** 삭제 **/
    @Test
    @DisplayName("상품 삭제는 Soft Delete 방식으로 진행한다.")
    void delete_ShouldSoftDelete() {
        // given
        Product product = getProduct();

        // when
        product.delete(1L);

        // then
        assertThat(product.getDeletedBy()).isEqualTo(1L);
        assertThat(product.getDeletedAt()).isNotNull();
    }
}
