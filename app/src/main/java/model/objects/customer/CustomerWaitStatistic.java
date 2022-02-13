package model.objects.customer;

import lombok.Data;

@Data
public class CustomerWaitStatistic {
    private long waitToGetInTime = 0;
    private long waitToGetOutTime = 0;
}
