# SpaceMaterial
Sample app applying modern architecture concepts (Rx, AutoValue, MVP, CLEAN, DI...) and Material design. It makes use of:
- http://wheretheiss.at/ API
- http://www.howmanypeopleareinspacerightnow.com/peopleinspace.json API

# USED IN THE APP (WIP)
- auto-value and auto-value-gson. Immutable objects.
- OkHttp logging-interceptor
- Retrofit 2 (with RxJava 1) + OkHttp 3 with cache-only and network-only strategies
- Bottom Navigation View from the Design Support Library
- Dagger 2 with dependant components
- MPV+R(Router)
- RxJava 2 (used only in Presenter-Interactors layers) implementing cool strategies (cache first, polling, offline mode)
- Proper code organization in packages and proper classes visibility
- Butterknife
- Android Palette (with Picasso)
- Picasso transformations
- Offline/Online transition management
- DiffUtil to avoid repainting in recyclerview
- Material transitions with shared elements

# TO DO/TO ADD IN THE APP
- Timber
- Vector animations
- Functional approach where possible
- Unit tests (DaggerMock?)
- Instrumentation tests
- Proguard
- pedrogvs/Renderers
- ...
