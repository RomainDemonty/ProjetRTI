# ProjetRTI

Site de marecher qui est accessible de pleins de manière différentes

#commande a executer pour ouvrir les ports sur la machine linux

pour ouvrir le port mysql
sudo firewall-cmd --permanent --zone=public --add-port=3306/tcp

pour ouvrir n'importe quel socket que on va utiliser (XXXX = numero de socket)
sudo firewall-cmd --permanent --zone=public --add-port=XXXX/tcp
pour relancer le firewall
sudo firewall-cmd --reload
