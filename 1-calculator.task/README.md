## 1. Калькулятор с четырьмя действиями [+20]
 
Входные данные содержат список выражений, разделенных переводом строк.
 
Каждое выражение состоит из чисел, разделенных операциями сложения (+),
вычитания (-), умножения (*) и деления (/).
 
Необходимо вычислить все выражения. Результаты разделить переводами строк.
 
Данные нужно читать со стандартного входа или из файла, переданного в качестве первого аргумента. Результат выдавать на стандартный выход или в файл, переданный в качестве второго аргумента.
 
Каждая строка, попадающая на вход, может быть выражением или же пустой строкой/комментарием.
 
Для чтения из ```./input/level-0.expr``` и записи результата в ```level-0.out``` необходимы следующие параметры:
 
```
./solution ./input/level-0.expr level-0.out
```
 
Если же запустить без параметров:
 
```
./solution
```
 
Чтение будет осуществляться с ```stdin```, а запись - в ```stdout```.
 
 
### Примеры работы
 
В примерах ниже префиксом ```>``` отмечены входные строки, а префиксом ```<``` - вывод калькулятора при работе в диалоговом режиме (stdin/stdout):
 
```
> 2 + 3
< 5
> 2 * 5 + 4
< 14
> 15/3
< 5
```
 
Если на вход передать следующий файл:
```
7 + 3
25 - 14
 
4 * 5
```
 
Результирующий файл будет:
 
```
10
11
20
```
 
 
### Бонусные задачи
 
Попробуйте реализовать для калькулятора следующие бонусные фичи:
 
* Операции в скобках выполняются перед операциями вне скобок
```
> 10 - (2 + 3)
< 5
```
 
* Пустые строки и строки, начинающиеся с #, игнорируются
* У операций умножения и деления приоритет выше, чем у сложения и вычитания
```
> 15 - 2 * 5
< 5
```
* Поддержка чисел с плавающей точкой в формате N.F (т. е. "3.1415")
* Поддержка негативных чисел в формате -N (т.е. "-5")
* Выводить сообщение о синтаксической ошибки в случае неправильной последовательности операторов (2 + + 3)
* Выводить сообщение о синтаксической ошибке в случае незакрытой скобки
 
----
