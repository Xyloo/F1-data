# F1 Data

## Uruchamianie
Wymagany jest Docker. W katalogu głównym projektu należy wpisać `docker compose up` i poczekać na ukończenie zadania (niestety niektóre obrazy są dość spore i trochę to trwa). Aplikacja działa, gdy pojawiają się już wiadomości ze Spring Boota.

## Korzystanie z aplikacji
Aplikacja jest dostępna pod adresem localhost:5000.
W aplikacji istnieje już dwóch użytkowników (username:password):
 - zwykły użytkownik: testUser:user
 - administrator: testAdmin:admin

Niezalogowany użytkownik otrzymuje dane tylko z 3 najnowszych wyścigów, zalogowany użykownik widzi wszystkie dane historyczne.
Administrator może usuwać oraz modyfikować innych użytkowników (poprzez endpointy, brak frontendu). Może także usunąć producenta (constructor).

Dostępny jest również Swagger (localhost:5000/swagger-ui/index.html) oraz możliwość zaimportowania API do np. Postmana (poprzez OpenAPI 3) - localhost:5000/v3/api-docs
