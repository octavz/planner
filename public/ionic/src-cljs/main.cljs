(ns starter
  (:use-macros
  [purnam.core :only [! def.n obj]]
  [gyr.core :only [def.module def.config def.value def.directive def.controller def.service]]))

(enable-console-print!)

(def.module main [ionic])

(def.config main [$stateProvider $urlRouterProvider]
  (doto $stateProvider
    (.state "app"
      #js {:url         "/app"
           :abstract    true
           :templateUrl "templates/menu.html"
           :controller  "AppCtrl"})
    (.state "app.main"
      #js {:url "/projects"
           :views
                #js {:menuContent
                     #js {:templateUrl "templates/projects.html"
                          :controller  "ProjectsCtrl"}}})
    )
  (-> $urlRouterProvider
    (.otherwise "/app/projects")))

(def.value main.baseUrl "http://localhost:9000/api/")
;(def.value main.baseUrl "http://planme.herokuapp.com/api/")

(def.controller main.ProjectsCtrl [$scope $rootScope $http baseUrl]
  (! $scope.projects
    #js [#js {:title "Test 1" :id 1}
         #js {:title "Test 2" :id 2}]))


(def.controller main.AppCtrl [$scope $ionicModal $timeout $http $ionicPopup $rootScope baseUrl]
  (! $scope.loadModals
    (fn [after]
      (do
        (-> (.fromTemplateUrl $ionicModal "templates/login.html" #js {:scope $scope :backdropClickToClose false})
          (.then (fn [m1]
                   (do
                     (! $scope.modalLogin m1)
                     (after))))))))

  ;init login dialog and show it once template loaded
  (! $scope.init
    (fn []
      (.loadModals $scope (fn [] (.showLogin $scope)))))

  (! $scope.showLogin
    (fn []
      (do
        (.show $scope.modalLogin))))

  ;init login form data
  (! $scope.loginData #js {:client_id     "1"
                           :grant_type    "password"
                           :client_secret "secret"
                           :username      "aaa@aaa.com"
                           :password      "123456"})

  (-> (.fromTemplateUrl $ionicModal "templates/register.html" #js {:scope $scope :backdropClickToClose false})
    (.then (fn [m2]
             (! $scope.modalRegister m2))))

  (! $scope.showRegister
    (fn []
      (do
        (.show $scope.modalRegister))))

  ;http request to login
  (! $scope.serverLogin
    (fn []
      (let [postData #js{:method  "POST"
                         :url     (str baseUrl "login")
                         :headers #js {:Accept       "application/json"
                                       :Content-Type "application/x-www-form-urlencoded"}
                         :data    (str "client_id=" (.-client_id $scope.loginData)
                                    "&grant_type=" (.-grant_type $scope.loginData)
                                    "&client_secret=" (.-client_secret $scope.loginData)
                                    "&username=" (.-username $scope.loginData)
                                    "&password=" (.-password $scope.loginData))}]
        (doto ($http postData)
          (.success (fn [res]
                      (do
                        (! $rootScope.user res)
                        (.hide $scope.modalLogin)
                        (prn (str "token: " (js/JSON.stringify $rootScope.user))))))
          (.error (fn [err] (js/alert err)))))))

  (! $scope.doLogin
    (fn [] (.serverLogin $scope)))

  ;register model
  (! $scope.registerData #js {:username        "aaa@aaa.com"
                              :passwordConfirm "123456"
                              :password        "123456"})

  ;http request to register
  (! $scope.serverRegister
    (fn []
      (let [postData #js {:login (.-username $scope.registerData) :password (.-password $scope.registerData)}]
        (-> $http
          (.post (str baseUrl "register") postData)
          (.success
            (fn [res]
              (if (or (= (.-errCode res) -1) (not (.-errCode res)))
                (do
                  (js/alert "Successfuly registered")
                  ;automatic login with register data
                  (set! (.-username $scope.loginData) (.-login postData))
                  (set! (.-password $scope.loginData) (.-password postData))
                  (.doLogin $scope)
                  (.closeRegister $scope))
                (js/alert (.-errMessage res)))))
          (.error
            (fn [err]
              (js/alert (str "Error registering: " err))))))))

  (! $scope.doRegister
    (fn []
      (.serverRegister $scope)))

  (! $scope.closeRegister
    (fn []
      (.hide $scope.modalRegister)))

  (! $scope.closeLogin
    (fn []
      (.hide $scope.modalLogin)))
  )
