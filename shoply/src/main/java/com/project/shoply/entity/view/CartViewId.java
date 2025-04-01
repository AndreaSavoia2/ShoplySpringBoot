package com.project.shoply.entity.view;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CartViewId implements Serializable {
    private Long cartId;
    private Long productId;

}
