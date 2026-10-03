# Lesona Lehibe - règles R8 (release)
# Garde les numéros de ligne pour des rapports d'erreur lisibles.
-keepattributes SourceFile,LineNumberTable,*Annotation*
# Pas d'obfuscation : projet ouvert, traces d'erreur directement lisibles.
-dontobfuscate
# Bibliothèque HTTP héritée (useLibrary org.apache.http.legacy)
-dontwarn org.apache.http.**
-dontwarn android.net.http.**
