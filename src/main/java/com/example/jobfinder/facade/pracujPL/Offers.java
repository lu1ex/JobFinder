package com.example.jobfinder.facade.pracujPL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offers {
    private List<PracujPLModel> offers;
    private int commonOffersCount;
}
