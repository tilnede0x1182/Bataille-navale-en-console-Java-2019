class Joueur {
	int numero_du_joueur;
	Affichage affichage;
	Menu menu;
	Utilitaire utilitaire;
	public boolean IA_humain;
	public Case [][] plateau;
	public Case [] cases_occupees;
	public Case [] cases_tentees;
	public int cases_tentees_indice;
	public boolean a_perdu;
	Bateau [] bateaux;
	public int derniere_case_tentee;
	Grille grille;
	/**
		true : Monter les deux cartes
		false : ne pas les montrer
	**/
	public boolean carte_a_montrer;

	public Joueur (int numero_du_joueur, Grille grille, boolean IA_humain, boolean carte_a_montrer) {
		constructeur(grille, numero_du_joueur, IA_humain, carte_a_montrer);
		place_bateaux();
	}

	public Joueur (int numero_du_joueur, Grille grille, Bateau[] bateaux, boolean IA_humain, boolean carte_a_montrer) {
		constructeur(grille, numero_du_joueur, IA_humain, carte_a_montrer);
		this.bateaux = bateaux;
		cases_occupees = utilitaire.cases_occuppees(this);
		plateau = utilitaire.construit_plateau(this);
	}

	public void constructeur (Grille grille, int numero_du_joueur, boolean IA_humain, boolean carte_a_montrer) {
		/**
			-1 : Pas de dernière case tentée
			 0 : dans l'eau
			 1 : touché
			 2 : coulé
		**/
		this.IA_humain = IA_humain;
		this.carte_a_montrer = carte_a_montrer;
		derniere_case_tentee = -1;
		a_perdu = false;
		this.grille = grille;
		this.numero_du_joueur = numero_du_joueur;
		utilitaire = new Utilitaire();
		affichage = new Affichage();
		menu = new Menu();
		cases_tentees = new Case[grille.hauteur*grille.largeur];
		cases_tentees_indice = 0;
	}

// ################ Fonctions utilitaires du jeu ################ //

	public Bateau place_bateau (int numero_du_bateau, int nombre_de_cases) {
		boolean placement_correct_incorrect = false;
		Case [] cases;
		int [] cases_bateau = null;
		int [] case_1 = new int[2];
		int [] case_fin = new int[2];
		while (!placement_correct_incorrect) {
			cases_bateau = menu.donne_cases_bateau(grille, numero_du_bateau, nombre_de_cases);
			case_1[0] = cases_bateau[0];
			case_1 [1] = cases_bateau[1];
			case_fin[0] = cases_bateau[2];
			case_fin[1] = cases_bateau[3];

			placement_correct_incorrect = utilitaire.verifie_placement_bateau (case_1, 
									case_fin, nombre_de_cases);
			if (!placement_correct_incorrect) {
				aff("Placement incorrect");
			}
		}
		cases = utilitaire.donne_cases_bateau(cases_bateau, nombre_de_cases);
		Bateau bateau = new Bateau(cases);
		return bateau;
	}

	/**
		Renvoie un int :
		0 : dans l'eau
		1 : touché
		2 : coulé
	**/
	public int attaquer_une_case (Case case_attaquee) {
		int i;
		int ordonnee_attaquee = utilitaire.formate_int(0, grille.hauteur, case_attaquee.ordonnee-1);
		int abscisse_attaquee = utilitaire.formate_int(0, grille.largeur, case_attaquee.abscisse-1);
		int bateau_attaque = -1;
		int derniere_case_tentee = -1;
		Case case_tmp = new Case();
		//aff("case_tmp : "+case_tmp.toString());

		for (i=0; i<cases_occupees.length; i++) {
			if (case_tmp.compare_case(cases_occupees[i], case_attaquee)) {
				cases_occupees[i].attaquer();
				//aff("cases_occupees[i] : "+cases_occupees[i].toString());
				
				derniere_case_tentee = 1;
			}
		}
		for (i=0; i<bateaux.length; i++) {
			if (bateaux[i].attaque(case_attaquee)) {
				bateau_attaque = i;
			}
			if (bateau_attaque!=-1) {
				if (bateaux[bateau_attaque].est_coule()) {
					derniere_case_tentee = 2;
					met_a_jour_plateau_coule();
				}
			}
		}
		plateau[ordonnee_attaquee][abscisse_attaquee].attaquer();
		//aff("plateau[ordonnee_attaquee][abscisse_attaquee] : "+plateau[ordonnee_attaquee][abscisse_attaquee].toString());
		if (derniere_case_tentee==-1) {
			this.derniere_case_tentee = 0;
			return 0;
		}
		else {
			this.derniere_case_tentee = derniere_case_tentee;
			return derniere_case_tentee;
		}
	}

	public void place_bateaux () {
		int i;
		boolean ok=false;
		Case case_tmp = new Case();
		Bateau bateau_tmp = new Bateau(), bateau_tmp2 = null;
		int [] tab_nombre_cases_bateaux = bateau_tmp.donne_nombre_de_cases_bateaux(this.grille);
		int nombre_de_bateaux = tab_nombre_cases_bateaux.length;
		this.bateaux = new Bateau[nombre_de_bateaux];
		affichage.afficher_carte_joueur(this.grille, this);
		for (i=0; i<nombre_de_bateaux; i++) {
			ok = false;
			while(!ok) {
				bateau_tmp2 = place_bateau((i+1), tab_nombre_cases_bateaux[i]);
				cases_occupees = utilitaire.cases_occuppees(this);
				if (!case_tmp.contient_case(cases_occupees, bateau_tmp2.cases)) ok=true;
				if (!ok)
					aff("Il y a déjà un bateau placé là");
				bateaux[i] = bateau_tmp2;
			}
			cases_occupees = utilitaire.cases_occuppees(this);
			plateau = utilitaire.construit_plateau(this);
			affichage.afficher_carte_joueur(this.grille, this);
		}
	}

	public int [][] genere_plateau_cases_tentees (Joueur joueur_attaquant) {
		int [][] plateau = joueur_attaquant.construit_plateau_cases_tentees();
		return plateau;
	}

	public void affiche_plateau_attaquant (Joueur joueur_attaquant) {
		Affichage affichage = new Affichage();

		int [][] plateau = genere_plateau_cases_tentees(joueur_attaquant);

		affichage.afficher_carte_attaque_joueur (plateau, joueur_attaquant.numero_du_joueur);
	}

	public boolean tenter_une_case_IA (Joueur joueur_attaque) {
		int etat_case = -1;
		IA ia_tmp = new IA(grille);
		if (carte_a_montrer) affiche_plateau_attaquant(this);
		Case case_tentee = new Case(-1, -1);
		// Vérifie s'il reste des cases à tenter sur le plateau.
		if (cases_a_tenter()) {
			case_tentee = ia_tmp.genere_une_case_aleat(this);
			aff("Joueur "+this.numero_du_joueur+" : ");
			aff("? = "+(char)(('A'+case_tentee.ordonnee-1))+case_tentee.abscisse);
			//aff(case_tentee.aff_case_str());
			etat_case = joueur_attaque.attaquer_une_case(case_tentee);
			ajoute_une_case_tentee(this, case_tentee, etat_case);
			return true;
		}
		else {
			aff("Joueur "+numero_du_joueur+", tenter_une_case : toutes les cases du plateau ont été tentées");
			return false;
		}
	}

	public boolean tenter_une_case (Joueur joueur_attaque) {
		int etat_case = -1;
		int [] tmp;
		IA ia_tmp = new IA(grille);
		boolean case_en_double = false;
		Case case_tentee = new Case(-1, -1);
		if (carte_a_montrer) affiche_plateau_attaquant(this);
		// Vérifie s'il reste des cases à tenter sur le plateau.
		if (cases_a_tenter()) {
			case_en_double = true;
			while (case_en_double) {
				tmp = menu.entre_case(grille, "Joueur "+numero_du_joueur+" : \nEntrer la case à jouer");
				case_tentee = new Case(tmp[0], tmp[1]);
				if (!utilitaire.verifie_case_tentee(this.cases_tentees, cases_tentees_indice, case_tentee)) {
					case_en_double = true;
					aff("Attention, la case "+case_tentee.case_format_ecrit()+" a déjà été tentée.");
					aff("Veuillez entrer une autre case :");
				}
				else case_en_double = false;
			}
			etat_case = joueur_attaque.attaquer_une_case(case_tentee);
			ajoute_une_case_tentee(joueur_attaque, case_tentee, etat_case);
			return true;
		}
		else {
			aff("Joueur "+numero_du_joueur+", tenter_une_case : toutes les cases du plateau ont été tentées");
			return false;
		}
	}

	/**
		Met à jour les bateaux coulés sur le plateau
	**/
	public void met_a_jour_plateau_coule () {
		int i, j;
		int ordonnee;
		int abscisse;

		for (i=0; i<bateaux.length; i++) {
			if (bateaux[i].est_coule()) {
				for (j=0; j<bateaux[i].cases.length; j++) {
					ordonnee = bateaux[i].cases[j].ordonnee-1;
					abscisse = bateaux[i].cases[j].abscisse-1;

					plateau[ordonnee][abscisse].couler();
				}
			}
		}
	}

	public void perd () {
		this.a_perdu = true;
	}

	public boolean a_perdu () {
		int i;

		if (a_perdu) return true;
		for (i=0; i<bateaux.length; i++) {
			//aff("bateaux[i].est_coule() : "+bateaux[i].est_coule());
			if (!bateaux[i].est_coule()) return false;
		}
		perd();
		return true;
	}

	/**
		0 : case non-tentée
		1 : case tentée
	**/
	public int [][] construit_plateau_cases_tentees () {
		int i, j;
		int ordonnee;
		int abscisse;
		int etat;

		Case case_tmp = new Case();

		int [][] plateau = new int[grille.hauteur][grille.largeur];

		// Initialisation du plateau
		for (i=0; i<plateau.length; i++) {
			for (j=0; j<plateau[i].length; j++) {
				plateau[i][j] = -1;
			}
		}

		// On met les cases tentées dans le plateau
		for (i=0; i<cases_tentees_indice; i++) {
			ordonnee = cases_tentees[i].ordonnee-1;
			abscisse = cases_tentees[i].abscisse-1;
			etat = cases_tentees[i].etat;

			plateau[ordonnee][abscisse] = etat;
		}
		return plateau;
	}

	public Case [] liste_des_cases_coulees () {
		int i, j, h;
		int nombre_de_cases = 0;

		Case [] res;
		for (i=0; i<bateaux.length; i++) {
			if (bateaux[i].coule)
				nombre_de_cases+=bateaux[i].nombre_de_cases;
		}
		res = new Case[nombre_de_cases];
		h = 0;
		for (i=0; i<bateaux.length; i++) {
			if (bateaux[i].coule) {
				for (j=0; j<bateaux[i].cases.length; j++) {
					res[h] = bateaux[i].cases[j];
					h++;
				}
			}
		}
		return res;
	}

	/**
		Met à jour les cases coulée dans les cases tentées 
		pour l'affichage de la carte des cases tentées
	**/
	public void met_a_jour_cases_coulees (Joueur joueur_attaque) {
		int i, j;
		Case case_tmp = new Case();

		Case [] cases_coulees = joueur_attaque.liste_des_cases_coulees();
		for (i=0; i<cases_coulees.length; i++) {
			for (j=0; j<cases_tentees_indice; j++) {
				if (case_tmp.compare_case(cases_coulees[i], cases_tentees[j])) {
					cases_tentees[j].couler(); 
					cases_tentees[j].etat = 2;
				}
			}
		}
	}

// ################### Fonctions utilitaires ###################### //

	/**
		Retourne true s'il reste des cases à tenter
		false sinon.
	**/
	public boolean cases_a_tenter () {
		return (cases_tentees_indice<cases_tentees.length);
	}

	public boolean ajoute_une_case_tentee (Joueur joueur_attaque, Case case_tentee, int etat_case) {
		if (etat_case==2)
			met_a_jour_cases_coulees(joueur_attaque);
		if (!cases_a_tenter()) return false;
		else {
			case_tentee.etat = etat_case;
			cases_tentees[cases_tentees_indice++] = case_tentee;
			return true;
		}
	}

	public void affiche_status_bateaux () {
		int i;

		for (i=0; i<bateaux.length; i++) {
			aff("Bateau "+(i+1)+" : ");
			bateaux[i].aff_cases_touchee();
		}
	}

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

	public String toString() {
		String res = ""+numero_du_joueur;
		return res;
	}

	public int get_numero_du_joueur () {
		return this.numero_du_joueur;
	}

	public void aff (String oo) {
		System.out.println(oo);
	}

	public void affnn (String oo) {
		System.out.print(oo);
	}
}