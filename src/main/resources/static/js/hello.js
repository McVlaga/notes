var app = angular.module("management", []);

// Controller Part
app.controller("controller", function ($scope, $http) {


    $scope.notes = [];
    $scope.noteForm = {
        id: -1,
        title: "",
        text: ""
    };

    _refreshNotes();

    $scope.submitNote = function () {

        var method = "POST";
        var url = '/notes';

        $http({
            method: method,
            url: url,
            data: angular.toJson($scope.noteForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    };

    $scope.createNote = function () {
        _clearFormData();
    }

    $scope.deleteNote = function (note) {
        $http({
            method: 'DELETE',
            url: '/notes/' + note.id
        }).then(_success, _error);
    };

    $scope.editNote = function (note) {
        $scope.noteForm.id = note.id;
        $scope.noteForm.title = note.title;
        $scope.noteForm.text = note.text;
    };

    function _refreshNotes() {
        $http({
            method: 'GET',
            url: '/notes'
        }).then(
            function (res) {
                $scope.notes = res.data;
            },
            function (res) {
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    function _success(res) {
        _refreshNotes();
        _clearFormData();
    }

    function _error(res) {
        var data = res.data;
        var status = res.status;
        var header = res.header;
        var config = res.config;
        alert("Error: " + status + ":" + data);
    }

    // Clear the form
    function _clearFormData() {
        $scope.noteForm.id = -1;
        $scope.noteForm.title = "";
        $scope.noteForm.text = ""
    };
});