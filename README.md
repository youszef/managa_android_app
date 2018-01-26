# manga_android_app
Simple manga android app using manga eden api


#Main activity bevat overzicht van alle manga's
#Deze word in database bijgehouden bij het eerste keer opstarten
#Een service wordt gestart omdat een call voor alle manga's van de server binnen te halen
in België 8 sec duurt. Te lang dus voor een asynctask.

Na de call wordt de gegevens weggeschreven en de listview opgevuld

geinspireerd door http://developer.android.com/training/displaying-bitmaps/display-bitmap.html

Voorlopig enkel bookmarks en top 10 -20 -30 mangas

Men kan bookmarks selecteren

View van listitem in main activity verandert zodra de smartphone roteert

 Bij het klikken komt men in een inhoudspagina van de geselecteerde manga
 met intro text
 tweede tab bevat alle chapters

 bij het klikken daarvan kan men bladeren door de pagina's

 dubbel tab zorgt ervoor dat de scherm maximaal gevuld wordt in de hoogte ofwel tot aan de breedte
 dan kan men swipen voor de pagina te verplaatsen


Omdat grotendeels werkt met internet kan er nietzoveel getest worden buiten Database

