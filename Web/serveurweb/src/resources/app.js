(function() {
    console.log("Mise a jour de la table");
    miseAJourTable();
})();

//const PropertiesReader = require('properties-reader');
//const properties = PropertiesReader('serveurweb\\properties.properties');

let ipServ = "localhost";//properties.get('serveurWeb');

document.getElementById('update').addEventListener("click", function(e) {
    e.stopPropagation();

    // Récupère l'ID de l'article
    var idArticle = document.getElementById('idArticle').value;

    // Récupère les valeurs mises à jour depuis le formulaire
    var nouveauPrix = document.getElementById('prixArticle').value;
    var nouvelleQuantite = document.getElementById('quantiteArticle').value;

    console.log(nouvelleQuantite);

    if(idArticle === 0)
    {
        let monTexte = document.getElementById("message");
        monTexte.innerHTML = "Aucun article selectionne";
        monTexte.style.color = "red";
    }
    else if( (nouveauPrix === "" || nouvelleQuantite === "") || (nouveauPrix < 0 || nouvelleQuantite < 0 ))
    {
        let monTexte = document.getElementById("message");
        monTexte.innerHTML = "Les champs ne peuvent pas etre inferieur a 0";
        monTexte.style.color = "red";
    }else{
        // Encode les données du formulaire
        var data = "id=" + idArticle +
            "&prix=" + encodeURIComponent(nouveauPrix) +
            "&stock=" + encodeURIComponent(nouvelleQuantite);

        // Crée une instance de l'objet XMLHttpRequest
        var xhr = new XMLHttpRequest();

        // Configure la requête POST vers le serveur
        xhr.open("POST", "http://" + ipServ + ":8081/api/tasks", true);
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

        // Gestionnaire d'événements de changement d'état
        xhr.onreadystatechange = function() {
            console.log(this);
            if (this.readyState === 4) {
                if (this.status === 201) {
                    console.log(this.response);
                    // Met à jour la table après la réussite de la requête POST
                    miseAJourTable();

                    // Affiche le message de réponse dans un élément avec l'ID "message"
                    let monTexte = document.getElementById("message");
                    monTexte.innerHTML = this.responseText;
                    monTexte.style.color = "green";
                } else {
                    alert("Une erreur est survenue...");
                }
            }
        };
        xhr.send(data);
    }


});

document.addEventListener("DOMContentLoaded", function() {
    var table = document.getElementById("maTable");
    var formulaire = document.getElementById("form");

    // Ajoute un gestionnaire d'événements à chaque ligne de la table
    table.addEventListener("click", function(e) {
        var ligne = e.target.closest("tr"); // Récupère la ligne cliquée
        if (ligne) {
            // Retire la classe "selected" de toutes les lignes
            var toutesLesLignes = table.getElementsByTagName("tr");
            for (var i = 0; i < toutesLesLignes.length; i++) {
                toutesLesLignes[i].classList.remove("selected");
            }

            // Ajoute la classe "selected" à la ligne sélectionnée
            ligne.classList.add("selected");

            // Récupère les valeurs des cellules de la ligne
            var id = ligne.cells[0].innerText;
            var intitule = ligne.cells[1].innerText;
            var prix = ligne.cells[2].innerText;
            var stock = ligne.cells[3].innerText;

            // Remplit les champs du formulaire avec les valeurs récupérées
            formulaire.elements["idArticle"].value = id;
            formulaire.elements["nomArticle"].value = intitule;
            formulaire.elements["prixArticle"].value = prix;
            formulaire.elements["quantiteArticle"].value = stock;

            var monTexte = document.getElementById("message");
            monTexte.innerHTML = "";

            var xhr = new XMLHttpRequest();
            var url = "http://localhost:8080/image/" + intitule + ".jpg";
            xhr.open("GET", url, true);

            xhr.responseType = "blob"; // Indique que la réponse sera sous forme de Blob

            xhr.onreadystatechange = function() {
                if (this.readyState === 4) {
                    if (this.status === 200) {
                        // La réponse du serveur est le contenu de l'image sous forme de Blob
                        var blob = this.response;

                        var imageArticle = document.getElementById("imageArticleSelect");
                        var imgElement = imageArticle.querySelector("img");

                        // Créez une URL à partir du Blob et attribuez-la à la source de l'image
                        imgElement.src = URL.createObjectURL(blob);
                    } else {
                        // Gestion des erreurs, si nécessaire
                        console.error("Erreur de requête : " + this.status);
                    }
                }
            };

            xhr.send();
        }
    });
});

function miseAJourTable()
{
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        console.log(this);
        if (this.readyState === 4) {
            if (this.status === 200) {
                console.log(this.response);
                var articles = this.response;
                console.log(articles);
                videTable();
                articles.forEach(function(article) {
                    ajouteLigne(article.id, article.intitule, article.prix, article.stock);
                });
            } else {
                alert("Une erreur est survenue...");
            }
        }
    };
    xhr.open("GET", "http://localhost:8081/api/tasks", true);
    xhr.responseType = "json";
    xhr.send();
}
function ajouteLigne(idArticle,nomArticle,prixArticle,quantiteArticle)
{
    var maTable = document.getElementById("maTable");
// Créer une nouvelle ligne
    var nouvelleLigne = document.createElement("tr");

// Créer des cellules
    let celluleId = document.createElement("td");
    celluleId.textContent = idArticle;

    let celluleNom = document.createElement("td");
    celluleNom.textContent = nomArticle;

    let cellulePrix = document.createElement("td");
    cellulePrix.textContent = prixArticle;

    let celluleQuantite = document.createElement("td");
    celluleQuantite.textContent = quantiteArticle;


// Ajouter les cellules à la ligne
    nouvelleLigne.appendChild(celluleId);
    nouvelleLigne.appendChild(celluleNom);
    nouvelleLigne.appendChild(cellulePrix);
    nouvelleLigne.appendChild(celluleQuantite);

// Ajouter la nouvelle ligne au tableau
    maTable.appendChild(nouvelleLigne);
}
function videTable()
{
    var maTable = document.getElementById("maTable");
    while (maTable.rows.length >= 1) {
        maTable.deleteRow(-1); // supprimer dernière ligne
    }
}
