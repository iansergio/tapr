## Atualizações feitas

## Endpoint - auth/login/magic/verify
Agora é um GET que recebe o token por parametro da URL e faz a validação diretamente do link de acesso recebido via email.

## Endpoint - auth/login/magic
Tem um servidor SMTP de Sandbox do Mailtrap configurado quando o ambiende é "prod", para configurar o seu servidor do Mailtrap deve alterar as variaveis SMTPhost, SMTPuser e SMTPpassword do arquivo SMTPMailsSender.java (poderia ter colocado isso em um env mas da uma trabalheira pra rodar o .env no codespace).
