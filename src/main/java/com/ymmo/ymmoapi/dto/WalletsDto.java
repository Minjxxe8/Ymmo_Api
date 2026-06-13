package com.ymmo.ymmoapi.dto;

public class WalletsDto {
    public record WalletModificationBalance(
            int balance
    ) {
    }

    public record WalletResponse(
            int id,
            double balance
    ) {
    }
}
