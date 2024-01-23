package ru.owen.app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyData {
    private String name;
    private String address;
    private String INN;
    private String KPP;

    @Override
    public String toString() {
        return name + ", " + INN + ", " + KPP + ", " + address;
    }
}
