package ua.mainacademy.model;


import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Item {
	private int id;
	private String name;
	private int code;
	private int price;
	private int initPrice;
	private String url;
	private  String imageUrl;
	private String group;

}
