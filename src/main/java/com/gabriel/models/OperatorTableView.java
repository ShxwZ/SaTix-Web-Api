package com.gabriel.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class OperatorTableView {
    private Long id;
    private String username;
    private String password;
    private LocalDateTime createdAt;

}
