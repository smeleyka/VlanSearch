# Vlansearch


Vlansearch - консольная утилита предназначенная для поиска вланов на управляемых свитчах


### Особенности
  * Быстрое сканирование всех узлов по заданному диапазону сети
  * Выводит список узлов с указанным вланом
  

### Как использовать
  * Пример на Ubuntu
```console
:~$ java -jar vlansearch.jar 121
 10.10.0.107            Rybnuy-2-2
   10.10.0.1                  Main
 10.10.1.227           Kytuzova_11
```
### Что сделать
 * Выбор сети (сейчас она захардкожена 10.10.0.0-10.10.2.255)
 * Сделать обработку диапазона вланов
 * Вывод всех найденных вланов по всем свитчам
 * Изменение дефолтного OID 

### Лицензия

Vlansearch is released under the [Apache 2.0 license](LICENSE).

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
