package ru.owen.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import ru.owen.app.model.KippriborMeyrtec.CommonPrice;
import ru.owen.app.model.Owen.OwenPrice;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "modification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"owenPrices", "kippriborMeyertecPrices"})
public class Modification {
    @Id
    @Column(name = "part_number")
    private String partNumber;

    @Column(name = "working_title")
    private String workingTitle;

    @Column(name = "modification")
    private String modification;

    @Column(name = "full_title")
    private String fullTitle;

    @Column(name = "price")
    private Double price_;

    @Column(name = "price_NDS")
    private Double priceNDS;

    @Column(name = "product_serial")
    private String productSerial;

    @Column(name = "group_")
    private String group;

    @Column(name = "delivery_time")
    private String deliveryTime;

    @Column(name = "size")
    private Integer size;

    @Column(name = "oversize")
    private Integer oversize;

    @Column(name = "multiplicity")
    private Integer multiplicity;

    @Column(name = "code_TNVED")
    private String codeTNVED;

    @Column(name = "status")
    private String status;

    @Column(name = "guarantee_period")
    private Short guaranteePeriod;

    @Column(name = "market_exit_date")
    private String MarketExitDate;

    @OneToMany(mappedBy = "modification")
    @JsonManagedReference("modification-owenprices")
//    @JsonIgnore
    private List<OwenPrice> owenPrices;

    @OneToMany(mappedBy = "modification")
//    @JsonManagedReference("modification-prices")
    @JsonIgnore
    private List<CommonPrice> kippriborMeyertecPrices;

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getWorkingTitle() {
        return workingTitle;
    }

    public void setWorkingTitle(String workingTitle) {
        this.workingTitle = workingTitle;
    }

    public String getModification() {
        return modification;
    }

    public void setModification(String modification) {
        this.modification = modification;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public Double getPrice_() {
        return price_;
    }

    public void setPrice_(Double price_) {
        this.price_ = price_;
    }

    public Double getPriceNDS() {
        return priceNDS;
    }

    public void setPriceNDS(Double priceNDS) {
        this.priceNDS = priceNDS;
    }

    public String getProductSerial() {
        return productSerial;
    }

    public void setProductSerial(String productSerial) {
        this.productSerial = productSerial;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getOversize() {
        return oversize;
    }

    public void setOversize(Integer oversize) {
        this.oversize = oversize;
    }

    public Integer getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(Integer multiplicity) {
        this.multiplicity = multiplicity;
    }

    public String getCodeTNVED() {
        return codeTNVED;
    }

    public void setCodeTNVED(String codeTNVED) {
        this.codeTNVED = codeTNVED;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Short getGuaranteePeriod() {
        return guaranteePeriod;
    }

    public void setGuaranteePeriod(Short guaranteePeriod) {
        this.guaranteePeriod = guaranteePeriod;
    }

    public String getMarketExitDate() {
        return MarketExitDate;
    }

    public void setMarketExitDate(String marketExitDate) {
        MarketExitDate = marketExitDate;
    }

    public List<OwenPrice> getOwenPrices() {
        return owenPrices;
    }

    public void setOwenPrices(List<OwenPrice> owenPrices) {
        this.owenPrices = owenPrices;
    }

    public List<CommonPrice> getKippriborMeyertecPrices() {
        return kippriborMeyertecPrices;
    }

    public void setKippriborMeyertecPrices(List<CommonPrice> kippriborMeyertecPrices) {
        this.kippriborMeyertecPrices = kippriborMeyertecPrices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Modification that = (Modification) o;
        return Objects.equals(partNumber, that.partNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partNumber);
    }
}
