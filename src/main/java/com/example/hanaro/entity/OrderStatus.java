package com.example.hanaro.entity;

public enum OrderStatus {

	PAYED, PREPARING, TRANSITING, DELIVERED;

	public OrderStatus getNextState() {
		return switch (this) {
			case PAYED -> PREPARING;
			case PREPARING -> TRANSITING;
			case TRANSITING -> DELIVERED;
			default -> throw new IllegalStateException("Cannot determine next status for: " + this);
		};
	}

	public int stateInterval() {
		return switch (this) {
			case PAYED -> 5;
			case PREPARING -> 15;
			case TRANSITING -> 60;
			default -> -1;
		};
	}
}
