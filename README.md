# Dummy API Example

This project is an example of a modular application to retrieve data from an external API (https://dummyapi.io).

It makes use of the following libraries:
* __Jetpack Compose__ for the UI
* __OkHttp / Retrofit__ for the REST requests
* __Koin__ for dependency injection
* __Room__ for database
* __Coil__ for image retrieval
* __Mockito Kotlin__ for some parts of unit testing

To build, you must add an application id for Dummy API in your __local.properties__ file:
```
api_key=1234567890abcdef12345678
```
See https://dummyapi.io to generate your own key.

## Architecture

The __DatasourceRepository__ interface is used to provide data to the application and have multiple implementations:
* __datasource-server__ to retrieve data from a remote REST service
* __datasource-room__ to retrieve and store data into a local Room database, by implementing
  __MutableDatasourceRepository__
* __datasource-cached__ is a proxy implementation that retrieve data from another DatasourceRepository implementation 
  and use a __MutableDatasourceRepository__ implementation as cache
  
Two other implementations are used for testing purposes:
* __datasource-test__ generate its own data and also provide some helper methods
* __datasource-memory__ is an implementation of __MutableDatasourceRepository__ which just store data in memory