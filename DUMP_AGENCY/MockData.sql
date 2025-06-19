-- ===================================
-- INSERTION DES OPTIONS DE VOYAGE AVEC VOS CATÉGORIES ENUM + VERSION
-- ===================================

INSERT INTO options (description, prix, category, version) VALUES 

-- ACCOMMODATION - Hébergement
('Surclassement chambre supérieure', 89.99, 'ACCOMMODATION', 0),
('Suite avec vue mer', 150.00, 'ACCOMMODATION', 0),
('Villa privée avec piscine', 299.99, 'ACCOMMODATION', 0),
('Chambre avec balcon privé', 75.00, 'ACCOMMODATION', 0),
('Appartement avec cuisine équipée', 125.00, 'ACCOMMODATION', 0),
('Bungalow sur pilotis', 189.99, 'ACCOMMODATION', 0),
('Riad traditionnel authentique', 145.00, 'ACCOMMODATION', 0),

-- TRAVEL - Voyages et déplacements
('Surclassement classe affaires', 899.99, 'TRAVEL', 0),
('Surclassement première classe', 1599.99, 'TRAVEL', 0),
('Vol direct sans escale', 299.99, 'TRAVEL', 0),
('Bagage supplémentaire 23kg', 45.00, 'TRAVEL', 0),
('Choix du siège prioritaire', 25.00, 'TRAVEL', 0),
('Assurance voyage premium', 39.99, 'TRAVEL', 0),

-- LUXURY - Services de luxe
('Chauffeur privé journée complète', 189.99, 'LUXURY', 0),
('Service de conciergerie 24h/24', 89.99, 'LUXURY', 0),
('Majordome personnel', 299.99, 'LUXURY', 0),
('Limousine avec chauffeur', 249.99, 'LUXURY', 0),
('Hélicoptère privé transfer', 599.99, 'LUXURY', 0),
('Yacht privé demi-journée', 899.99, 'LUXURY', 0),

-- EVENT - Événements spéciaux
('Dîner spectacle folklorique', 79.99, 'EVENT', 0),
('Concert de musique classique', 65.00, 'EVENT', 0),
('Soirée gala avec animation', 125.00, 'EVENT', 0),
('Participation festival local', 55.00, 'EVENT', 0),
('Célébration anniversaire privée', 189.99, 'EVENT', 0),
('Cérémonie traditionnelle locale', 68.00, 'EVENT', 0),

-- MEALS - Restauration
('Petit-déjeuner continental inclus', 25.00, 'MEALS', 0),
('Demi-pension (petit-déjeuner + dîner)', 65.00, 'MEALS', 0),
('Pension complète (tous repas inclus)', 95.00, 'MEALS', 0),
('Dîner gastronomique restaurant étoilé', 165.00, 'MEALS', 0),
('Dégustation de vins locaux', 48.00, 'MEALS', 0),
('Barbecue sur la plage au coucher du soleil', 72.00, 'MEALS', 0),
('Pique-nique gourmet', 28.00, 'MEALS', 0),
('Cours de cuisine locale', 68.00, 'MEALS', 0),
('Brunch dominical illimité', 42.00, 'MEALS', 0),

-- WELLNESS - Bien-être
('Accès spa et centre de bien-être', 45.00, 'WELLNESS', 0),
('Massage relaxant (1h)', 79.99, 'WELLNESS', 0),
('Massage aux pierres chaudes', 95.00, 'WELLNESS', 0),
('Massage en couple', 159.99, 'WELLNESS', 0),
('Soins du visage premium', 89.99, 'WELLNESS', 0),
('Hammam traditionnel', 35.00, 'WELLNESS', 0),
('Sauna finlandais privatisé', 55.00, 'WELLNESS', 0),
('Séance de yoga privée', 68.00, 'WELLNESS', 0),
('Méditation guidée au lever du soleil', 25.00, 'WELLNESS', 0),

-- TRANSPORT - Transport local
('Location de voiture économique', 35.00, 'TRANSPORT', 0),
('Location de voiture premium', 75.00, 'TRANSPORT', 0),
('Transfert aéroport privé', 55.00, 'TRANSPORT', 0),
('Location de moto/scooter', 25.00, 'TRANSPORT', 0),
('Vélos électriques pour la durée du séjour', 45.00, 'TRANSPORT', 0),
('Pass transport public illimité', 28.00, 'TRANSPORT', 0),
('Taxi privé disponible 24h/24', 125.00, 'TRANSPORT', 0),

-- OTHERS - Services divers
('Wi-Fi haut débit illimité', 15.00, 'OTHERS', 0),
('Service de blanchisserie express', 25.00, 'OTHERS', 0),
('Service de bagagerie', 18.00, 'OTHERS', 0),
('Téléphone local avec données', 35.00, 'OTHERS', 0),
('Parking privé sécurisé', 18.00, 'OTHERS', 0),
('Accès lounge VIP aéroport', 45.00, 'OTHERS', 0),
('Service de réveil personnalisé', 8.00, 'OTHERS', 0),

-- ACTIVITIES - Activités et excursions
('Visite guidée de la ville historique', 42.00, 'ACTIVITIES', 0),
('Safari photo en 4x4', 125.00, 'ACTIVITIES', 0),
('Plongée sous-marine (baptême)', 85.00, 'ACTIVITIES', 0),
('Tour en hélicoptère (30min)', 295.00, 'ACTIVITIES', 0),
('Randonnée guidée en montagne', 55.00, 'ACTIVITIES', 0),
('Excursion en bateau avec déjeuner', 89.00, 'ACTIVITIES', 0),
('Vol en montgolfière au lever du soleil', 189.99, 'ACTIVITIES', 0),
('Kayak en eaux vives', 65.00, 'ACTIVITIES', 0),
('Parapente tandem', 149.99, 'ACTIVITIES', 0),
('Équitation en pleine nature', 78.00, 'ACTIVITIES', 0),
('Cours de surf avec matériel', 89.99, 'ACTIVITIES', 0),

-- SERVICES - Services professionnels
('Guide privé multilingue', 199.99, 'SERVICES', 0),
('Photographe professionnel (demi-journée)', 149.99, 'SERVICES', 0),
('Photographe professionnel (journée)', 259.99, 'SERVICES', 0),
('Audioguide en français', 15.00, 'SERVICES', 0),
('Traducteur personnel', 125.00, 'SERVICES', 0),
('Assistance médicale 24h/24', 45.00, 'SERVICES', 0),
('Service d\'étage 24h/24', 55.00, 'SERVICES', 0),

-- PACKAGE - Packages thématiques
('Package lune de miel romantique', 299.99, 'PACKAGE', 0),
('Package famille avec enfants', 189.99, 'PACKAGE', 0),
('Package aventure extrême', 345.00, 'PACKAGE', 0),
('Package détente et bien-être', 259.99, 'PACKAGE', 0),
('Package culturel et patrimoine', 199.99, 'PACKAGE', 0),
('Package gastronomique', 245.00, 'PACKAGE', 0),
('Package sports nautiques', 189.99, 'PACKAGE', 0),
('Package montagne et randonnée', 165.00, 'PACKAGE', 0),
('Package découverte urbaine', 149.99, 'PACKAGE', 0),
('Package photographie professionnelle', 399.99, 'PACKAGE', 0);

use prod;
-- ===================================
-- INSERTION DES VOYAGES AVEC VERSION
-- ===================================

INSERT INTO trips (destination_country, destination_continent, destination_city, minimum_duration, description, unit_price, version) VALUES 

-- EUROPE
('FRANCE', 'EUROPE', 'PARIS', 3, 'Découvrez la Ville Lumière : Tour Eiffel, Louvre, Champs-Élysées. Un voyage romantique au cœur de l''art et de la culture française.', 899, 0),
('ITALY', 'EUROPE', 'ROME', 4, 'Plongez dans l''histoire antique : Colisée, Vatican, Fontaine de Trevi. Rome vous attend avec ses trésors millénaires.', 749, 0),
('SPAIN', 'EUROPE', 'BARCELONA', 3, 'Architecture unique de Gaudí, plages méditerranéennes et tapas authentiques. Barcelone, entre modernisme et tradition.', 649, 0),
('GREECE', 'EUROPE', 'SANTORINI', 5, 'Couchers de soleil époustouflants, villages blancs et bleus, cuisine méditerranéenne. Santorin, la perle des Cyclades.', 1299, 0),
('PORTUGAL', 'EUROPE', 'LISBON', 4, 'Tramways jaunes, azulejos colorés, fado authentique. Lisbonne vous charme par son authenticité.', 599, 0),
('UNITED_KINGDOM', 'EUROPE', 'LONDON', 4, 'Palais royaux, Big Ben, musées exceptionnels. Londres, capitale cosmopolite entre tradition et modernité.', 799, 0),
('NETHERLANDS', 'EUROPE', 'AMSTERDAM', 3, 'Canaux romantiques, musées d''art, vélos partout. Amsterdam, la Venise du Nord aux mille charmes.', 679, 0),
('GERMANY', 'EUROPE', 'BERLIN', 4, 'Histoire contemporaine, art de rue, vie nocturne. Berlin, métropole dynamique au passé fascinant.', 589, 0),

-- ASIE
('JAPAN', 'ASIE', 'TOKYO', 7, 'Métropole futuriste entre tradition et modernité : temples, sushi, technology. Tokyo, l''expérience japonaise ultime.', 1899, 0),
('THAILAND', 'ASIE', 'BANGKOK', 6, 'Temples dorés, marchés flottants, massage thaï. Bangkok, porte d''entrée vers l''Asie du Sud-Est.', 1199, 0),
('INDIA', 'ASIE', 'DELHI', 8, 'Palais moghols, épices parfumées, spiritualité intense. Delhi, voyage au cœur de l''Inde mystique.', 999, 0),
('CHINA', 'ASIE', 'BEIJING', 6, 'Grande Muraille, Cité Interdite, canard laqué. Pékin, capitale impériale aux mille facettes.', 1399, 0),
('VIETNAM', 'ASIE', 'HANOI', 7, 'Baie d''Halong, pho authentique, histoire coloniale. Vietnam, beauté naturelle et culturelle.', 899, 0),
('SOUTH_KOREA', 'ASIE', 'SEOUL', 5, 'K-pop, palais anciens, technologie de pointe. Séoul, modernité asiatique et traditions séculaires.', 1299, 0),
('SINGAPORE', 'ASIE', 'SINGAPORE', 4, 'Cité-État futuriste, jardins verticaux, fusion culinaire. Singapour, melting-pot moderne.', 1499, 0),

-- AMERIQUE_DU_NORD
('USA', 'AMERIQUE_DU_NORD', 'NEW_YORK', 5, 'Big Apple : Central Park, Broadway, Statue de la Liberté. New York, la ville qui ne dort jamais.', 1599, 0),
('CANADA', 'AMERIQUE_DU_NORD', 'MONTREAL', 4, 'Charme européen en Amérique : Vieux-Montréal, poutine, festivals. Montréal, métropole bilingue.', 1099, 0),
('USA', 'AMERIQUE_DU_NORD', 'LOS_ANGELES', 5, 'Hollywood, plages de Malibu, style de vie californien. Los Angeles, ville des anges et des stars.', 1399, 0),
('CANADA', 'AMERIQUE_DU_NORD', 'VANCOUVER', 5, 'Montagnes et océan, nature urbaine, qualité de vie. Vancouver, perle du Pacifique canadien.', 1199, 0),

-- AMERIQUE_DU_SUD
('BRAZIL', 'AMERIQUE_DU_SUD', 'RIO_DE_JANEIRO', 6, 'Christ Rédempteur, plages de Copacabana, samba. Rio, la Cidade Maravilhosa.', 1299, 0),
('ARGENTINA', 'AMERIQUE_DU_SUD', 'BUENOS_AIRES', 5, 'Tango, bœuf argentin, architecture européenne. Buenos Aires, Paris de l''Amérique du Sud.', 1099, 0),
('PERU', 'AMERIQUE_DU_SUD', 'CUSCO', 8, 'Machu Picchu, culture inca, altitude mystique. Pérou, aventure archéologique exceptionnelle.', 1599, 0),
('CHILE', 'AMERIQUE_DU_SUD', 'SANTIAGO', 6, 'Cordillère des Andes, vignobles, gastronomie. Santiago, élégance chilienne au pied des montagnes.', 1199, 0),

-- AFRIQUE
('MOROCCO', 'AFRIQUE', 'MARRAKECH', 5, 'Médina colorée, tajines savoureux, désert du Sahara. Marrakech, porte du désert.', 799, 0),
('EGYPT', 'AFRIQUE', 'CAIRO', 7, 'Pyramides de Gizeh, Nil éternel, trésors de Toutânkhamon. Égypte, berceau de la civilisation.', 1199, 0),
('SOUTH_AFRICA', 'AFRIQUE', 'CAPE_TOWN', 8, 'Table Mountain, vignobles, safari Big Five. Afrique du Sud, diversité naturelle extraordinaire.', 1499, 0),
('KENYA', 'AFRIQUE', 'NAIROBI', 9, 'Safari Masai Mara, migration des gnous, culture masaï. Kenya, royaume de la savane.', 1799, 0),
('TUNISIA', 'AFRIQUE', 'TUNIS', 5, 'Carthage antique, médina authentique, hospitalité tunisienne. Tunisie, carrefour méditerranéen.', 699, 0),

-- OCEANIE
('AUSTRALIA', 'OCEANIE', 'SYDNEY', 10, 'Opéra House, Grande Barrière de Corail, Outback. Australie, continent aux mille merveilles.', 2299, 0),
('NEW_ZEALAND', 'OCEANIE', 'AUCKLAND', 12, 'Fjords majestueux, All Blacks, seigneur des anneaux. Nouvelle-Zélande, nature à l''état pur.', 2499, 0),
('FIJI', 'OCEANIE', 'SUVA', 7, 'Paradis tropical, récifs coralliens, culture mélanésienne. Fidji, évasion dans le Pacifique Sud.', 1899, 0);

-- ===================================
-- ASSOCIATION DES 3 OPTIONS PAR DÉFAUT À TOUS LES VOYAGES
-- ===================================

INSERT INTO trip_options (trip_id, option_id)
SELECT t.id, o.optionId 
FROM trips t 
CROSS JOIN options o 
WHERE o.description IN (
    'Demi-pension (petit-déjeuner + dîner)',
    'Assurance voyage premium', 
    'Guide privé multilingue'
);

-- ===================================
-- ASSOCIATIONS D'OPTIONS SPÉCIFIQUES PAR TYPE DE DESTINATION
-- ===================================

-- Destinations romantiques (Paris, Santorini, Venise) - Options romantiques
INSERT INTO trip_options (trip_id, option_id)
SELECT t.id, o.optionId 
FROM trips t, options o 
WHERE t.destination_city IN ('PARIS', 'SANTORINI', 'ROME')
AND o.description IN (
    'Package lune de miel romantique',
    'Dîner gastronomique restaurant étoilé',
    'Suite avec vue mer',
    'Massage en couple'
);

-- Destinations asiatiques - Options culturelles et gastronomiques
INSERT INTO trip_options (trip_id, option_id)
SELECT t.id, o.optionId 
FROM trips t, options o 
WHERE t.destination_continent = 'ASIE'
AND o.description IN (
    'Cours de cuisine locale',
    'Package culturel et patrimoine',
    'Cérémonie traditionnelle locale',
    'Hammam traditionnel'
);

-- Destinations urbaines (New York, London, Tokyo) - Options premium
INSERT INTO trip_options (trip_id, option_id)
SELECT t.id, o.optionId 
FROM trips t, options o 
WHERE t.destination_city IN ('NEW_YORK', 'LONDON', 'TOKYO')
AND o.description IN (
    'Surclassement classe affaires',
    'Tour en hélicoptère (30min)',
    'Service de conciergerie 24h/24',
    'Limousine avec chauffeur'
);

-- Destinations balnéaires et tropicales - Options détente
INSERT INTO trip_options (trip_id, option_id)
SELECT t.id, o.optionId 
FROM trips t, options o 
WHERE t.destination_city IN ('SANTORINI', 'RIO_DE_JANEIRO', 'SYDNEY', 'SUVA', 'BARCELONA')
AND o.description IN (
    'Package détente et bien-être',
    'Package sports nautiques',
    'Plongée sous-marine (baptême)',
    'Bungalow sur pilotis',
    'Yacht privé demi-journée'
);

-- Destinations safari et aventure - Options aventure
INSERT INTO trip_options (trip_id, option_id)
SELECT t.id, o.optionId 
FROM trips t, options o 
WHERE t.destination_city IN ('NAIROBI', 'CAPE_TOWN', 'CUSCO')
AND o.description IN (
    'Safari photo en 4x4',
    'Package aventure extrême',
    'Location de voiture premium',
    'Villa privée avec piscine',
    'Randonnée guidée en montagne'
);
