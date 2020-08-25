(function(){
    'use strict'
    var app = angular.module('app', []);
    function controllerDaSchutzer($scope, $http){
        var self = this;
        self.curso = {};
        self.cursos = [];
        self.combo = {};
        
        var cursos = [];
        
        // Carregar Categorias
        $.ajax({
            url: 'http://localhost:8080/api/v1/categorias/',
            async: true,
            crossDomain: true,
            contentType: 'application/json',
            cache: false,
            method: 'GET',
            dataType: 'json',
            processData: false,
            success: function(data) {
               self.completarCombo(data);
            }
        });
        
        // Carrega a tabela
        $.ajax({
            url: 'http://localhost:8080/api/v1/cursos',
            async: true,
            crossDomain: true,
            contentType: 'application/json',
            cache: false,
            method: 'GET',
            dataType: 'json',
            processData: false,
            success: function(data) {
                self.inserirTabela(data);
            }
        });
        
        self.atualizarTabela = function() {
            
            self.cursos = [];
            
            // Carrega a tabela
            $.ajax({
                url: 'http://localhost:8080/api/v1/cursos',
                async: true,
                crossDomain: true,
                contentType: 'application/json',
                cache: false,
                method: 'GET',
                dataType: 'json',
                processData: false,
                success: function(data) {
                    console.log(data);
                    self.inserirTabela(data);
                    $scope.$apply();
                }
            });
        };
        
        self.inserirTabela = function(data) {
            
            for(var i = 0; i < data.length; i++) {
                self.cursos.push({
                    "id": data[i].id,
                    "descricao": data[i].descricao,
                    "dataInicio": data[i].dataInicio.toLocaleString().split('T')[0],
                    "dataTermino": data[i].dataTermino.toLocaleString().split('T')[0],
                    "quantidadeAluno": data[i].quantidadeAluno,
                    "categoria": {
                        "descricao": data[i].categoria.descricao,
                        "id": data[i].categoria.id
                    }
                });
                $scope.$apply();
            }
        };
       
        self.salvar = function() {
            
            if (self.curso.assunto && self.curso.inicio && self.curso.fim && $('.cat :selected').text()) {
                if (self.curso.inicio < self.curso.fim) {
                    var dia_i = self.curso.inicio.toLocaleString().split('/')[0];
                    var mes_i = self.curso.inicio.toLocaleString().split('/')[1];
                    var ano_i = self.curso.inicio.toLocaleString().split('/')[2].split(' ')[0];

                    var dia_f = self.curso.fim.toLocaleString().split('/')[0];
                    var mes_f = self.curso.fim.toLocaleString().split('/')[1];
                    var ano_f = self.curso.fim.toLocaleString().split('/')[2].split(' ')[0];

                    var cursoNovo = {
                        "descricao": self.curso.assunto,
                        "dataInicio": ano_i + '-' + mes_i + '-' + dia_i,
                        "dataTermino": ano_f + '-' + mes_f + '-' + dia_f,
                        "quantidadeAluno": self.curso.qtd || 0,
                        "categoria": {
                            "descricao": $('.cat :selected').text(),
                            "id": $('.cat :selected').val()
                        }
                    };

                    $.ajax({
                        url: 'http://localhost:8080/api/v1/cursos',
                        async: true,
                        crossDomain: true,
                        contentType: 'application/json',
                        cache: false,
                        method: 'POST',
                        dataType: 'json',
                        processData: false,
                        data: JSON.stringify(cursoNovo),
                        success: function(data) {
                            self.cursos.push({
                                "id": data.id,
                                "descricao": self.curso.assunto,
                                "dataInicio": ano_i + '-' + mes_i + '-' + dia_i,
                                "dataTermino": ano_f + '-' + mes_f + '-' + dia_f,
                                "quantidadeAluno": self.curso.qtd || 0,
                                "categoria": {
                                    "id": $('.cat :selected').val(),
                                    "descricao": $('.cat :selected').text()
                                }
                            });
                            $scope.$apply();

                            $(".categoria_alt").find("option[text=" + $('.cat :selected').text() + "]").attr("selected", true);
                        },
                        error: function(error) {
                            alert(error.responseText);
                        }
                    });
                } else {
                    alert("* Data de início maior que Data de término!");
                }
            } else {
                alert("* Descrição do assunto, categoria, data de início e término são campos obrigatórios!");
            }
        };
        
        self.excluir = function(id) {
            $.ajax({
                url: 'http://localhost:8080/api/v1/cursos/' + id,
                async: true,
                crossDomain: true,
                contentType: 'application/json',
                cache: false,
                method: 'DELETE',
                dataType: 'json',
                processData: false,
                success: function(data) {
                    self.atualizarTabela();
                }
            });

//            self.cursos = [];
//            self.atualizarTabela();
//            $scope.$apply();
        };
        
        self.pesquisar = function(descricao) {
            console.log(descricao);
            $.ajax({
                url: 'http://localhost:8080/api/v1/cursos/find',
                async: true,
                crossDomain: true,
                contentType: 'application/json',
                cache: false,
                method: 'POST',
                dataType: 'json',
                processData: false,
                data: descricao,
                success: function(data) {
                    self.cursos = [];
                    $scope.$apply();
                    
                    self.inserirTabela(data);
                },
                error: function(data){
                    self.cursos = [];
                    $scope.$apply();
                }
            });
        };
        
        self.limpar = function() {
            $('#assunto_p').val("");
            self.atualizarTabela();
        };
        
        self.atualizar = function(data) {
            
            var dia_i, mes_i, ano_i;
            var dia_t, mes_t, ano_t;
            if(data.dataI) {
                dia_i = data.dataI.toLocaleString().split('T')[0].split('/')[0];
                mes_i = data.dataI.toLocaleString().split('T')[0].split('/')[1];
                ano_i = data.dataI.toLocaleString().split('T')[0].split('/')[2].split(' ')[0];
            } else {
                dia_i = data.dataInicio.split('-')[2];
                mes_i = data.dataInicio.split('-')[1];
                ano_i = data.dataInicio.split('-')[0];
            }
            
            if(data.dataT) {
                dia_t = data.dataT.toLocaleString().split('T')[0].split('/')[0];
                mes_t = data.dataT.toLocaleString().split('T')[0].split('/')[1];
                ano_t = data.dataT.toLocaleString().split('T')[0].split('/')[2].split(' ')[0];
            } else {
                dia_t = data.dataTermino.split('-')[2];
                mes_t = data.dataTermino.split('-')[1];
                ano_t = data.dataTermino.split('-')[0];
            }
            
            if (dia_i <= dia_t && mes_i <= mes_t && ano_i <= ano_t ||
                    dia_i > dia_t && mes_i < mes_t && ano_i <= ano_t) {
                var cursoAtualizado = {
                    "id": data.id,
                    "descricao": data.descricao,
                    "dataInicio": ano_i + '-' + mes_i + '-' + dia_i,
                    "dataTermino": ano_t + '-' + mes_t + '-' + dia_t,
                    "quantidadeAluno": data.quantidadeAluno || 0,
                    "categoria": {
                        "descricao": $('.categoria_alt :selected').text(),
                        "id": $('.categoria_alt :selected').val()
                    }
                };

                $.ajax({
                    url: 'http://localhost:8080/api/v1/cursos/' + data.id,
                    async: true,
                    crossDomain: true,
                    contentType: 'application/json',
                    cache: false,
                    method: 'PUT',
                    dataType: 'json',
                    processData: false,
                    data: JSON.stringify(cursoAtualizado),
                    success: function(data) {

                    },
                    error: function(erro) {
                        alert(erro);
                    }
                });
            } else {
                alert("* Data de início maior que Data de término!");
            }
        };
        
        self.completarCombo = function(data) {
            for(var i = 0; i < data.length; i++) {
                if (i === 0) {
                    $('.cat').append('<option selected value="' + data[i].id + '">' + data[i].descricao + '</option>');
                } else {
                    $('.cat').append('<option value="' + data[i].id + '">' + data[i].descricao + '</option>');
                }
            }
        };
          
    }
    app.controller('ctrl',['$scope', '$http', controllerDaSchutzer]);
    
})();