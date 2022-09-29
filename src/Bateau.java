class Bateau {
	Menu menu;
	int nombre_de_cases;
	boolean touche;
	boolean coule;

	public Case [] cases;

	public Bateau() {
		menu = new Menu();
	}

	/**
		Cases : 
			Liste des cases du plateau 
			sur lesquel est le bateau.
	**/
	public Bateau (Case [] cases) {
		menu = new Menu();
		this.nombre_de_cases = cases.length;
		this.cases = cases;
		touche = false;
		coule = false;
	}

// ######## Fonctions utilitaires pour les bateaux ######### //

// ###************** Placement des bateaux **************### //

	/**
		Renvoie le nombre de cases max.

		Soit n le nombre de joueurs
		et m le nombre de cases total
		de la grille.

		Calcul : res = (1/(n+1))*m
	**/
	public int nombre_de_place_de_bateau_max (Grille grille) {
		int hauteur = grille.hauteur;
		int largeur = grille.largeur;

		int nombre_de_cases_total = hauteur*largeur;
		int max_autorise = (int)((3.0/5.0)*nombre_de_cases_total);

		return max_autorise;
	}

	/**
		Donne la taille max d'un bateau
		en fonction de la hauteur et 
		de la largeur de la grille uniquement.
	**/
	public int taille_max_bateau (Grille grille, int proposition) {
		int hauteur = grille.hauteur;
		int largeur = grille.largeur;
		int max_hauteur_largeur = Math.min(hauteur, largeur);

		if (proposition>max_hauteur_largeur) return max_hauteur_largeur;
		else return proposition;
	}

	/**
		Donne les bateaux d'un joueur
		Chaque case du tableau int[] 
		contient le nombre du case d'un bateau.
	**/
	public int [] donne_nombre_de_cases_bateaux (Grille grille) {
		int nombre_de_place_de_bateau_max = nombre_de_place_de_bateau_max(grille);

		int [] res = menu.entre_nombre_de_case_bateaux(grille, nombre_de_place_de_bateau_max);		

		return res;
	}

// ################ Fonctions utilitaires du jeu ################ //

	public boolean attaque (Case case_a_attaquer) {
		int i;
		Case case_tmp = new Case();
		boolean est_coule;
		if (!case_a_attaquer.contient_case(this.cases, 
						case_a_attaquer)) {
			return false;
		}
		for (i=0; i<cases.length; i++) {
			if (case_tmp.compare_case(cases[i], case_a_attaquer)) {
				cases[i].attaquer();
			}
		}
		this.coule = verifie_coule();		

		return true;
	}

	public boolean verifie_coule () {
		int i;

		for (i=0; i<cases.length; i++) {
			if (!cases[i].est_attaquee()) {
				return false;
			}
		}
		for (i=0; i<cases.length; i++) {
			cases[i].couler();
		}
		return true;
	}

	public boolean est_coule () {
		return coule;
	}

// ################### Fonctions utilitaires ###################### //

	public void aff_cases () {
		int i;

		for (i=0; i<cases.length; i++) {
			aff("Case "+(i+1)+" : \n"+cases[i].aff_case_str());
		}
	}

	public void aff_cases_touchee () {
		int i;

		for (i=0; i<cases.length; i++) {
			if (cases[i].est_attaquee())
				aff("cases["+(i+1)+"] est attaquée");
			else 
				aff("cases["+(i+1)+"] n'est pas attaquée");
		}
	}

	public void aff (String oo) {
		System.out.println(oo);
	}

	public void affnn (String oo) {
		System.out.print(oo);
	}

}