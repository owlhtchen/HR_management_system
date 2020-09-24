package model.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompanyManager implements Serializable {
    private List<Company> companies = new ArrayList<>();
    private Integer companyID;

    public CompanyManager() {
        this.companyID = 100;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void addCompany(Company company){
        company.setCompanyID(companyID);
        this.companies.add(company);
        companyID++;
    }

}
