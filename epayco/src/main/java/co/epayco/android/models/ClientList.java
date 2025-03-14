package co.epayco.android.models;

public class ClientList {
    String page, perPage;

    public ClientList() {}

    public ClientList(
        String page,
        String perPage
    )
    {
        this.page = page;
        this.perPage = perPage;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPerPage() {
        return perPage;
    }

    public void setPerPage(String perPage) {
        this.perPage = perPage;
    }
}
