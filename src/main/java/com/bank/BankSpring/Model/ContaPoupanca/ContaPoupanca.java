package com.bank.BankSpring.Model.ContaPoupanca;

import com.bank.BankSpring.Model.ContaBancaria.ContaBancaria;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CONTA_BANCARIA")
public class ContaPoupanca extends ContaBancaria {
}