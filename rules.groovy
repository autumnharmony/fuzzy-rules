activation(20)
rule('0') {
    _if_ = [{(DX - D(0)).greaterThan days(30)}]
    _then_ = {
        X     = 'Проверка работоспособности системы в целом'
    }
}
rule('1') {
    _if_ = [{(D(1) - DX).greaterThan days(30*3)}]
    _then_ = {
        X     = 'Проверка и диагностика датчиков пожарной безопасности'
    }
}
rule('2') {
    _if_ = [{(DX - D(2)).greaterThan days(182)}]
    _then_ = {
        X     = 'Проверка системы речевого оповещения'
    }
}
rule('3') {
    _if_ = [{(DX - D(3)).greaterThan days(3*365)}]
    _then_ = {
        X     = 'Проверка системы с \nгенерацией тревоги'
    }
}
rule('4') {
    _if_ = [{(DX - D(4)).greaterThan days(365)}]
    _then_ = {
        X     = 'Проверка кабельных систем.'
    }
}
rule('5') {
    _if_ = [{CR}]
    _then_ = {
        X     = 'Внеплановая проверка'
    }
}
rule('6') {
    _if_ = [{(DX - D(6)).greaterThan days(30*4)}]
    _then_ = {
        X     = 'Проверка системы пожаротушения'
    }
}
