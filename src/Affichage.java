class Affichage {
	int nombre_d_espaces;

	public Affichage () {
		this.nombre_d_espaces = 5;
	}

	public void afficher_carte_attaque_joueur (int [][] plateau, int numero_du_joueur) {
		char charactere = 'A';
		String tmp_str_1="";
		int i, j, h;
		int tmp_int_1 = 1;
		int hauteur = plateau.length;
		int largeur = plateau[0].length;

		aff("\nLégende : \n\n  0 : case pas encore attaquée\n  X : Dans l'eau\n  1 : Touché\n  2 : Coulé");
		aff("\nTableau d'attaque du joueur "+numero_du_joueur+" : \n");

		affiche_espaces(nombre_d_espaces+1);
		for (i=0; i<largeur; i++) {
			affnn(""+(i+1));
			if (i>(9-1))
				affiche_espaces(nombre_d_espaces-1);
			else
				affiche_espaces(nombre_d_espaces);
		}
		affiche_sauts_de_ligne(1);

		affiche_sauts_de_ligne(1);
		for (i=0; i<hauteur; i++) {
			affnn(""+(charactere++));
			affiche_espaces(nombre_d_espaces);
			for (j=0; j<largeur; j++) {
				if (plateau[i][j]==-1)
					tmp_str_1 = "0";
				else if (plateau[i][j]==0)
					tmp_str_1 = "X";
				else 
					tmp_str_1 = ""+plateau[i][j];
				tmp_int_1 = 1;
				affnn(tmp_str_1);
				affiche_espaces(nombre_d_espaces-(tmp_int_1-1));
			}
			affiche_sauts_de_ligne(2);
		}
	}

	public void afficher_carte_joueur (Grille grille, Joueur joueur) {
		if (!joueur.carte_a_montrer) return;
		char c = 'A';
		String tmp_str_1="";
		int i, j, h;
		int tmp_int_1 = 1;
		int numero_du_joueur = joueur.get_numero_du_joueur();
		Case [][] plateau = joueur.plateau;
		int hauteur = grille.hauteur;
		int largeur = grille.largeur;

		aff("\nTableau du joueur "+numero_du_joueur+" : \n");

		affiche_espaces(nombre_d_espaces+1);
		for (i=0; i<largeur; i++) {
			affnn(""+(i+1));
			if (i>(9-1))
				affiche_espaces(nombre_d_espaces-1);
			else
				affiche_espaces(nombre_d_espaces);
		}
		affiche_sauts_de_ligne(1);

		affiche_sauts_de_ligne(1);
		for (i=0; i<hauteur; i++) {
			affnn(""+(c++));
			affiche_espaces(nombre_d_espaces);
			for (j=0; j<largeur; j++) {
				if (plateau!=null) {
					tmp_str_1 = plateau[i][j].toString();
					tmp_int_1 = tmp_str_1.length();
					affnn(tmp_str_1);
				}
				else affnn("0");
				affiche_espaces(nombre_d_espaces-(tmp_int_1-1));
			}
			affiche_sauts_de_ligne(2);
		}
	}


// ################### Fonctions utilitaires ###################### //

	public void affiche_espaces (int nombre_d_espaces) {
		int i;

		for (i=0; i<nombre_d_espaces; i++) {
			affnn(" ");
		}
	}

	public void affiche_sauts_de_ligne (int nombre_de_lignes) {
		int i;

		for (i=0; i<nombre_de_lignes; i++) {
			affnn("\n");
		}
	}

	public void aff (String oo) {
		System.out.println(oo);
	}

	public void affnn (String oo) {
		System.out.print(oo);
	}
}