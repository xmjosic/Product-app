package hr.xmjosic.product.dao;

import hr.xmjosic.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for {@link Product}. */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
