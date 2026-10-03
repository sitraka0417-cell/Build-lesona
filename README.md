# Lesona Lehibe

Application Android (Java, sans dépendance externe) de lecture des leçons de l'école du sabbat,
avec versets cliquables, surlignage de texte et navigation par swipe.

## Compiler (GitHub Actions)

- **Actions → Build APK → Run workflow** : produit l'APK *debug* et l'APK *release* (onglet *Artifacts*).
- **Publier une version** : créer un tag, par exemple `v1.1.0` → une *Release* GitHub est créée avec l'APK.
- Le numéro de build GitHub devient le `versionCode` (mises à jour toujours possibles par-dessus).

## Signature

Par défaut l'APK est signé avec `app/debug.keystore` (signature stable entre les builds).
Pour une clé personnelle, ajouter ces secrets dans **Settings → Secrets and variables → Actions** :
`RELEASE_KEYSTORE_BASE64`, `RELEASE_KEYSTORE_PASSWORD`, `RELEASE_KEY_ALIAS`, `RELEASE_KEY_PASSWORD`.

## Code

- `LesonaActivity.java` : lecteur de leçon.
- `ReaderTouchHandler.java` : arbitre des gestes (swipe / tap sur verset / sélection pour surligner).
- `MainActivity.java` : liste des leçons et mises à jour.

## Mise à jour des leçons (bouton "down")

Le bouton ouvre un popup : anneau de progression en %, puis
- erreur réseau : « Misy olana ny fifandraisana » + « Anandrana indray » / « Ajanona »
- succès : anneau vert + « Vita ny fanavaozana » + « Akatona »
- rien de neuf : « Mbola tsy misy lesona vaovao » + « Akatona »

Code : `SyncPopup.java` (interface) et la synchro dans `MainActivity.java`. Compatible Android 5.0+ (minSdk 21).
