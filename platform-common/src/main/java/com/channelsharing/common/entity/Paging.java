package com.channelsharing.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Paging<T> implements Serializable {
	private static final long serialVersionUID = 1464694386266977339L;

	private int count;
	private List<T> rows = new ArrayList<>();

}
