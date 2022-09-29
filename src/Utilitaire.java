class Utilitaire {

	public Utilitaire() {}

//################ Fonctions utilitaires du jeu ################//

	public boolean tmp () {
		return true;
	}

	public boolean compare_coordonneee (int ordonnee_source, int abscisse_source, int ordonnee_but, int abscisse_but) {
		return (ordonnee_source==ordonnee_but &&
			abscisse_source==abscisse_but);
	}

	/**
		Un bateau peut être soit horizontal ou vertical soit oblique.
		Cas :

		case_fin est égale à case_1
		case_fin à gauche de case_1
		case_fin à droite de case_1
		case_fin en haut de case_1
		case_fin en bas de case_1		
	**/
	public boolean verifie_placement_bateau (int [] case_1, int [] case_fin, int nombre_de_cases) {
		int tmp = 0;

		if (case_1[0]==case_fin[0] && case_1[1]==case_fin[1]) {
			if (nombre_de_cases==1) return true;
			else return false;
		}
		//Horizontal
		if (case_1[0]==case_fin[0]) {
			//case_fin à gauche de case_1
			if (case_fin[1]<case_1[1]) {
				tmp = case_1[1]-case_fin[1];
				if (tmp==nombre_de_cases && tmp>0) return true;
				else return false;
			}
			//case_fin à droite de case_1
			if (case_fin[1]>case_1[1]) {
				tmp = case_fin[1]-case_1[1];
				if (tmp==nombre_de_cases && tmp>0) return true;
				else return false;
			}
		}
		//Vertical
		if (case_1[1]==case_fin[1]) {
			//case_fin en haut de case_1
			if (case_fin[0]<case_1[0]) {
				tmp = case_1[1]-case_fin[1];
				if (tmp==nombre_de_cases && tmp>0) return true;
				else return false;
			}
			//case_fin en base de case_1
			if (case_fin[0]>case_1[0]) {
				tmp = case_fin[1]-case_1[1];
				if (tmp==nombre_de_cases && tmp>0) return true;
				else return false;
			}
		}
		return false;
	}

	/**
		Prend un tableau de 4 int tel que :
		t[0] = case_1_ordonnee
		t[1] = case_1_abscisse
		t[2] = case_fin_ordonnee
		t[3] = case_fin_abscisse

		renvoie l'ensemble des cases du bateau.
	**/
	public Case [] donne_cases_bateau (int [] cases, int nombre_de_cases) {
		int i;
		int [] case_1 = {cases[0], cases[1]};
		int [] case_fin = {cases[2], cases[3]};;
		Case [] res = new Case[nombre_de_cases];

		if (case_1[0]==case_fin[0] && case_1[1]==case_fin[1]) {
			res[0] = new Case(cases[0], cases[1]);
		}
		//Horizontal
		if (case_1[0]==case_fin[0]) {
			//case_fin à gauche de case_1
			if (case_fin[1]<case_1[1]) {
				for (i=0; i<nombre_de_cases; i++) {
					res[i] = new Case(case_1[0], case_1[1]-i);
				}
			}
			//case_fin à droite de case_1
			if (case_fin[1]>case_1[1]) {
				for (i=0; i<nombre_de_cases; i++) {
					res[i] = new Case(case_1[0], case_1[1]+i);
				}
			}
		}
		//Vertical
		if (case_1[1]==case_fin[1]) {
			//case_fin en haut de case_1
			if (case_fin[0]<case_1[0]) {
				for (i=0; i<nombre_de_cases; i++) {
					res[i] = new Case(case_1[0]-i, case_1[1]);
				}
			}
			//case_fin en bas de case_1
			if (case_fin[0]>case_1[0]) {
				for (i=0; i<nombre_de_cases; i++) {
					res[i] = new Case(case_1[0]+i, case_1[1]);
				}
			}
		}
		return res;
	}

	/**
		Remplit un tableau de Case[] des cases 
		occupées pour rendre accessible un Bateau, 
		une Case et un Joueur à partir d'un même objet.
		
	**/
	public Case [] cases_occuppees (Joueur joueur) {
		int i, j, h;
		int nombre_de_bateaux_initialises = 0;
		int ordonnee, abscisse;
		int nombre_total_de_cases = 0;
		Case [] res;
		Bateau [] bateaux = joueur.bateaux;
		for (i=0; i<bateaux.length; i++) {
			if (bateaux[i]!=null) {
				nombre_de_bateaux_initialises+=1;
			}
		}

		/**
			Compte le nombre total de cases.
		**/
		for (i=0; i<nombre_de_bateaux_initialises; i++) {
			if (bateaux[i]!=null)
				nombre_total_de_cases+=bateaux[i].cases.length;
		}
		res = new Case[nombre_total_de_cases];
		h = 0;
		for (i=0; i<nombre_de_bateaux_initialises; i++) {
			if (bateaux[i]!=null) {
				for (j=0; j<bateaux[i].cases.length; j++) {
					if (bateaux[i]!=null) {
						ordonnee = bateaux[i].cases[j].ordonnee;
						abscisse = bateaux[i].cases[j].abscisse;
						res[h] = new Case(joueur, bateaux[i], 
							(i+1), ordonnee, abscisse);
					h++;
					}
				}
			}
		}
		return res;		
	}

	/**
		Si trouve la case recherchée, la retourne,
		sinon retourne null.
	**/
	public Case cherche_case (Case [] cases, Case case_1) {
		int i;
		Case res = null;

		for (i=0; i<cases.length; i++) {
			if ((new Case()).compare_case (cases[i], case_1)) return cases[i];
		}
		return res;
	}

	/**
		Construit un plateau pour chaque joueur
		en fonction de ses bateaux.
	**/
	public Case [][] construit_plateau (Joueur joueur) {
		int i, j;
		Case case_tmp, case_tmp2;

		Case [][] plateau = new Case[joueur.grille.hauteur][joueur.grille.largeur];

		for (i=0; i<plateau.length; i++) {
			for (j=0; j<plateau[i].length; j++) {
				case_tmp = new Case((i+1), (j+1));
				case_tmp2 = cherche_case(joueur.cases_occupees, case_tmp);
				if (case_tmp2!=null)
					plateau[i][j] = case_tmp2;
				else plateau[i][j] = case_tmp;
			}
		}
		return plateau;
	}

	/**
		Recherche le premier joueur du tableau 
		qui n'a pas perdu. Renvoie son emplacement 
		dans le tableau.
	**/
	public int recherche_gagnant (Joueur [] joueurs) {
		int i;

		for (i=0; i<joueurs.length; i++) {
			if (!joueurs[i].a_perdu()) return i;
		}
		return -1;
	}

	/**
		Retourne false si la case tentée a déjà été tentée,
		true sinon.
	**/
	public boolean verifie_case_tentee (Case [] cases_tentees, int cases_tentees_indice, Case case_tentee) {
		int i;
		Case case_tmp = new Case();

		for (i=0; i<cases_tentees_indice; i++) {
			if (case_tmp.compare_case(cases_tentees[i], case_tentee)) return false;
		}
		return true;
	}

	public String convertit_coordonnees_str (int ordonnee, int abscisse) {
		return ""+(char)('A'+ordonnee-1)+abscisse;
	}

	/**
		-1 : rien
		 0 : dans l'eau
		 1 : touché
		 2 : coulé
	**/
	public void afficher_evenement_coup (int tmp) {
		String res = "";

		if (tmp==0) res = "Dans l'eau !";
		if (tmp==1) res = "Touché !";
		if (tmp==2) res = "Coulé !";

		aff("\n\t\t"+res+"\n");
	}

//################### Fonctions utilitaires ####################//

	public void aff_tab (String nom, Case [] cases) {
		aff_tab(nom, cases, cases.length);
	}

	public void aff_tab (String nom, Case [] cases, int cases_tentees_indice) {
		int i;
		int cases_tentees_indice_tmp = Math.min(cases_tentees_indice, cases.length);

		for (i=0; i<cases_tentees_indice_tmp; i++) {
			if (cases[i]==null)
				aff(nom+"["+i+"] = null");
			else aff(nom+"["+i+"] = \n"+cases[i].aff_case_str());
		}
	}

	public void aff_tab (String nom, int [] tab) {
		int i;
		for (i=0; i<tab.length; i++) {
			aff(nom+"["+i+"] = "+tab[i]);
		}
	}

	public boolean is_integer (String s0) {
		try {
			int n1 = Integer.parseInt(s0);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public int randInt (int min, int max) {
		int res = (int)(Math.random()*max)+min;
		if (res>max) res = max;
		if (res<min) res = min;

		return res;
	}

	public int formate_int (int min, int max, int tmp) {
		int res = tmp;
		if (res<min)
			res = min;
		if (res>max)
			res = max;
		return res;
	}

	public void aff (String oo) {
		System.out.println(oo);
	}

	public void affnn (String oo) {
		System.out.print(oo);
	}
}