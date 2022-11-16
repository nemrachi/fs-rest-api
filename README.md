### Zadanie - FileSystem REST API

**Napíšte Java aplikáciu, ktorá sprístupní file system operácie pomocou REST API.
Aplikácia podporuje tieto operácie.**
- Create new Directory
- Delete existing Directory
- Create new File
- Delete existing File
- Copy File to target Directory
- Move File to target Directory
- List content of Directory
  - content is ordered by size
- Get content of File
- Encrypt the content of a file with symmetric encryption
- Decrypt the content of a file with symmetric encryption
- Search for ‘pattern’ in file content
  - get the list of files where the given pattern occurs
  - get the line number (per file) where the given pattern occurs

**Forma odovzdania:**\
Maven / Gradle projekt skompilovatelný pomocou JDK 8 alebo novším.

Projekt musi byt otestovaný automatizovanými testami. Definícia / Dokumentácia REST API musí
byť dostupná vo formáte OpenAPI / Swagger.