package Modele;

public class Article {

    private int idAliment;
    private String intitule;
    private float prix;
    private int quantite;

    public Article()
    {
        setidAliment(-1);
        setintitule("Vide");
        setprix(0);
        setquantite(0);
    }

    public Article(int Al , String inti , float p , int q)
    {
        setidAliment(Al);
        setintitule(inti);
        setprix(p);
        setquantite(q);
    }

    public void setidAliment(int idAliment){
        this.idAliment = idAliment;
    }

    public void setintitule(String intitule){
        this.intitule = intitule;
    }

    public void setprix(float prix) {
        this.prix = prix;
    }

    public void setquantite(int quantite) {
        this.quantite = quantite;
    }

    public int getIdAliment() {
        return idAliment;
    }

    public String getIntitule(){
        return intitule;
    }

    public float getPrix() {
        return prix;
    }

    public int getQuantite() {
        return quantite;
    }

    @Override
    public String toString() {
        return  getIdAliment() + " " + getIntitule() + " " + getQuantite() + " " + getPrix();
    }
}
