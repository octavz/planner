(ns planner.views.login)

(def viewLogin [:html
  {:ng-app "myAppLogin", :lang "en"}
  [:head
   [:meta {:charset "utf-8"}]
   [:title "Planner App Login"]
   [:link {:href "/bootstrap/css/bootstrap.min.css", :rel "stylesheet"}]
   [:link
    {:href "/bootstrap/css/bootstrap-theme.min.css", :rel "stylesheet"}]
   [:link {:href "css/app.css", :rel "stylesheet"}]]
  [:body
   [:script {:src "/lib/jquery-2.1.0.min.js"}]
   [:script {:src "/bootstrap/js/bootstrap.js"}]
   [:script {:src "/lib/angular/angular.js"}]
   [:script {:src "/lib/angular/angular-resource.js"}]
   [:script {:src "/lib/underscore/underscore.js"}]
   [:script {:src "/lib/underscore/angular-underscore.js"}]
   [:script {:src "/js/appLogin.js"}]
   [:div.container
    [:div.col-sm-8.col-sm-offset-2
     [:div.Login
      [:h4 "Login"]
      [:form
       {:action "/login", :novalidate "novalidate", :name "loginForm", :method "POST"}
       [:div.form-group
        {:ng-class
         "{ 'has-error' : loginForm.email.$invalid && !loginForm.email.$pristine }"}
        [:label {:for "email"} "Email"]
        [:input.form-control
         {:_ "_",
          :required "required",
          :ng-model "user.Email",
          :value "aaa@aaa.com",
          :name "username",
          :type "email"}]
        [:p.help-block
         {:ng-show
          "loginForm.email.$invalid && !loginForm.email.$pristine"}
         "Invalid Email."]]
       [:div.form-group
        {:ng-class
         "{ 'has-error' : loginForm.password.$invalid && !loginForm.password.$pristine }"}
        [:label {:for "password"} "Password"]
        [:input#PasswordID.form-control
         {:ng-minlength "6",
          :required "required",
          :ng-model "user.Password",
          :name "password",
          :value "123456"
          :type "password"}]
        [:p.help-block
         {:ng-show "loginForm.password.$error.minlength"}
         "Password is too short."]]
       [:input.btn.btn-primary
        {:value "Sign In",
         :ng-disabled "false",
         ;:ng-disabled "loginForm.$invalid",
         :type "submit"}]
       [:span "&nbsp;"]
       [:a
        {:href "/index.html#/register"}
        [:input.btn.btn-primary
         {:value "Register", :type "button"}]]]]]]]])
