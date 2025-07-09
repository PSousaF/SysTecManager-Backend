package br.com.systechmanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orcamento")
public class Budget {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String deviceType;
	private String model;
	private int quantity;
	private int idResp;
	private int idCli;
	private String deviceBrand;
	private String serie;
	private String defect;
	private String review;
	private String possibleCauses;
	private String observation;
	private String valueItem;
	private String cost;
	private String detail;
	private String situation;
	private String aparroved;
	private String pieces;
	private String finish;
	private String stating;
	private String bid;

}
