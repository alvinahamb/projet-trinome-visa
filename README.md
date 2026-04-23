# projet-trinome-visa

# Sprint 1 :  Enregitrement demande de visa

- Type de visa demander :
  - investisseur : statut de societe, extrait d inscription au registre de commerce, carte fiscale
  - travailleur : autorisation emploi, attestaion d emploi


### **4. Pas de validations métier importantes**

* ✅ Validation dates passeport
* ❌ **ABSENT**: Validations métier comme:
  * Passeport expiré? ([dateExpiration < today()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html))
  * Visa transformable expiré? ([dateFin < today()](vscode-file://vscode-app/usr/share/code/resources/app/out/vs/code/electron-browser/workbench/workbench.html))
  * Âge minimum du demandeur?
  * Passeport unique par demandeur?
  * Demandeur peut-il avoir plusieurs demandes actives?
