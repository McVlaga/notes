var app = angular.module("management", []);

// Controller Part
app.controller("controller", function ($scope, $http) {

    $scope.showNewCard = false;
    $scope.notes = [];
    $scope.newNote = {};
    $scope.allowEditMode = true;
    $scope.noteForm = {
        title: "",
        text: ""
    };

    _refreshNotes();

    $scope.submitNote = function () {
        $http({
            method: "POST",
            url: '/notes',
            data: angular.toJson($scope.noteForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    };

    $scope.createNote = function () {
        $scope.showNewCard = true;
    };

    $scope.updateNote = function (note) {
        $scope.noteForm.title = note.title;
        $scope.noteForm.text = note.text;
        $http({
            method: "PUT",
            url: '/notes/' + note.id,
            data: angular.toJson($scope.noteForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
        $scope.allowEditMode = true;
    };

    $scope.deleteNote = function (note) {
        $http({
            method: 'DELETE',
            url: '/notes/' + note.id
        }).then(_success, _error);
    };

    $scope.editNote = function (note) {
        if($scope.allowEditMode) {
            $scope.newNote = angular.copy(note);
            $scope.allowEditMode = false;
        }
    };

    $scope.cancel = function (index) {
        $scope.notes[index] = $scope.newNote;
        $scope.allowEditMode = true;
    };

    $scope.onSearchChange = function(searchTerm) {
        if (!searchTerm) {
            _refreshNotes();
        } else {
            _searchBy(searchTerm);
        }
    };

    function _searchBy(searchTerm) {
        $http({
            method: 'GET',
            url: '/notes/search=' + searchTerm
        }).then(
            function (res) {
                $scope.notes = res.data;
                $scope.showNewCard = false;
            },
            function (res) {
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    function _refreshNotes() {
        $http({
            method: 'GET',
            url: '/notes'
        }).then(
            function (res) {
                $scope.notes = res.data;
                $scope.showNewCard = false;
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
        alert("Error: " + status + ":" + angular.toJson(data));
    }

    // Clear the form
    function _clearFormData() {
        $scope.noteForm.id = -1;
        $scope.noteForm.title = "";
        $scope.noteForm.text = ""
    }

    $scope.parseDate = function (date) {
        return new Date(date);
    }
});