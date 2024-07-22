package com.javaspring.curso.config;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.javaspring.curso.entities.Category;
import com.javaspring.curso.entities.Order;
import com.javaspring.curso.entities.OrderItem;
import com.javaspring.curso.entities.Payment;
import com.javaspring.curso.entities.Product;
import com.javaspring.curso.entities.User;
import com.javaspring.curso.entities.enums.OrderStatus;
import com.javaspring.curso.repositories.CategoryRepo;
import com.javaspring.curso.repositories.OrderItemRepo;
import com.javaspring.curso.repositories.OrderRepo;
import com.javaspring.curso.repositories.ProductRepo;
import com.javaspring.curso.repositories.UserRepo;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private OrderRepo orderRepo;

  @Autowired
  private CategoryRepo categoryRepo;

  @Autowired
  private ProductRepo productRepo;

  @Autowired
  private OrderItemRepo orderItemRepo;

  @Override
  public void run(String... args) throws Exception {

    User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");
    User u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", "123456");
    userRepo.saveAll(Arrays.asList(u1, u2));

    Order o1 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.DELIVERED, u1);
    Order o2 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), OrderStatus.SHIPPED, u2);
    Order o3 = new Order(null, Instant.parse("2019-07-22T15:21:22Z"), OrderStatus.WAITING_PAYMENT, u1);
    Order o4 = new Order(null, Instant.parse("2019-07-22T15:21:22Z"), OrderStatus.PAID, u2);
    orderRepo.saveAll(Arrays.asList(o1, o2, o3, o4));

    Category cat1 = new Category(null, "Electronics");
    Category cat2 = new Category(null, "Books");
    Category cat3 = new Category(null, "Computers");
    categoryRepo.saveAll(Arrays.asList(cat1, cat2, cat3));

    Product p1 = new Product(null, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
    Product p2 = new Product(null, "Smart TV", "Nulla eu imperdiet purus. Maecenas ante.", 2190.0, "");
    Product p3 = new Product(null, "Macbook Pro", "Nam eleifend maximus tortor, at mollis.", 1250.0, "");
    Product p4 = new Product(null, "PC Gamer", "Donec aliquet odio ac rhoncus cursus.", 1200.0, "");
    Product p5 = new Product(null, "Rails for Dummies", "Cras fringilla convallis sem vel faucibus.", 100.99, "");
    productRepo.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

    p1.getCategories().add(cat2);
    p2.getCategories().add(cat1);
    p2.getCategories().add(cat3);
    p3.getCategories().add(cat3);
    p4.getCategories().add(cat3);
    p5.getCategories().add(cat2);
    productRepo.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

    OrderItem oi1 = new OrderItem(o1, p1, 2);
    OrderItem oi2 = new OrderItem(o1, p3, 1);
    OrderItem oi3 = new OrderItem(o2, p3, 2);
    OrderItem oi4 = new OrderItem(o3, p5, 2);
    OrderItem oi5 = new OrderItem(o4, p4, 2);
    OrderItem oi6 = new OrderItem(o4, p2, 2);
    orderItemRepo.saveAll(Arrays.asList(oi1, oi2, oi3, oi4, oi5, oi6));

    Payment pay1 = new Payment(null, Instant.parse("2019-06-21T06:45:07Z"), o1);
    Payment pay2 = new Payment(null, Instant.parse("2019-07-21T13:27:10Z"), o2);
    Payment pay3 = new Payment(null, Instant.parse("2019-07-21T13:27:10Z"), o4);
    o1.setPayment(pay1);
    o2.setPayment(pay2);
    o4.setPayment(pay3);
    orderRepo.saveAll(Arrays.asList(o1, o2, o4));

    System.out.println("----------------------" + o1.getId());
  }
}
