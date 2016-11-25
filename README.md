# WhichOne App

## Introduction

WhichOne is small social network for creating quizzes and sharing them with everyone. The main idea of this application is to help people          to make desicion in daily life (e.g. "Which movie to watch?", "Which drink to buy?"). Also this app can help to know ***independent***  opinion in any questions (e.g. "Which photo is better?") and express your own point of view (***completely anonymous!***).

The application is based on **MVP**(Model-View-Presenter) pattern.

### Dependencies:

* **Retrofit** - makes HTTP requests
* **ORMLite** - provides mapping Java objects to database
* **RxJava** - enables to easily work with asynchronous events
* **Dagger** - injects dependencies
* **Glide** - loads images
* **ButterKnife** - binds views
* **Robolectric** - is used for testing
* **Retrolambda** - allows to use lambda expressions

### Getting Started

Download this repository, import to Android Studio, let Gradle to configurate project. Run.

To run app with local server you need:

1. Run server on your local machine ([for more details](https://github.com/l-o-b-s-t-e-r/which-one-server))

2. Change **BASE_USL** in *RequestServiceImpl.java*

### Requirements

* Platform Version: Android 4.4

### Screenshots

<img src="https://github.com/l-o-b-s-t-e-r/which-one-android/blob/master/screenshots/log_in_screen.png" width="288">
<img src="https://github.com/l-o-b-s-t-e-r/which-one-android/blob/master/screenshots/wall_screen.png" width="288">
<img src="https://github.com/l-o-b-s-t-e-r/which-one-android/blob/master/screenshots/home_wall_screen.png" width="288">
<img src="https://github.com/l-o-b-s-t-e-r/which-one-android/blob/master/screenshots/detail_screen.png" width="288">
<img src="https://github.com/l-o-b-s-t-e-r/which-one-android/blob/master/screenshots/new_screen.png" width="288">
<img src="https://github.com/l-o-b-s-t-e-r/which-one-android/blob/master/screenshots/demo.gif" width="288" height="579">


