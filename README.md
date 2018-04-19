# Teaching-HEIGVD-RES-2018-Labo-SMTP

## Contexte

MailRobot java application a été développé pour le cours RES, @HEIG-VD.



## Description

Cette application permet d'envoyer automatiquement les emails spam. Ces mails sont construit à partir d'un groupe de victimes stockés dans un fichier texte contenant des adresses mails de ces personnes. Un groupe consiste en un émetteur et un set de recepteurs pris au hasard.

L'application va alors créer un mail forgé dont le corps du message est lu dans un fichier texte contenant une liste de messages. Un message est pris de manière aléatoire.

## Utilisation

L'application communique avec un serveur SMTP local à travers le port 25. On peut modifier les propriétés du serveur à partir du fichier de configuration(config.properties) situé à la racine du projet.

On peut également changer l'ensemble des adresses emails(vitims.utf8) des victimes ou les messages de plaisanterie(messages.utf8).




## Deliverables

## Evaluation

* See CyberLearn.








