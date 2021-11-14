package net.cacheux.dummyapi.datasource.server

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

const val USER_LIST = """
{
    "data": [
        {
            "id": "60d0fe4f5311236168a109ca",
            "title": "ms",
            "firstName": "Sara",
            "lastName": "Andersen",
            "picture": "https://randomuser.me/api/portraits/women/58.jpg"
        },
        {
            "id": "60d0fe4f5311236168a109cb",
            "title": "miss",
            "firstName": "Edita",
            "lastName": "Vestering",
            "picture": "https://randomuser.me/api/portraits/med/women/89.jpg"
        },
        {
            "id": "60d0fe4f5311236168a109cc",
            "title": "ms",
            "firstName": "Adina",
            "lastName": "Barbosa",
            "picture": "https://randomuser.me/api/portraits/med/women/28.jpg"
        },
        {
            "id": "60d0fe4f5311236168a109cd",
            "title": "mr",
            "firstName": "Roberto",
            "lastName": "Vega",
            "picture": "https://randomuser.me/api/portraits/med/men/25.jpg"
        },
        {
            "id": "60d0fe4f5311236168a109ce",
            "title": "mr",
            "firstName": "Rudi",
            "lastName": "Droste",
            "picture": "https://randomuser.me/api/portraits/med/men/83.jpg"
        },
        {
            "id": "60d0fe4f5311236168a109cf",
            "title": "mrs",
            "firstName": "Carolina",
            "lastName": "Lima",
            "picture": "https://randomuser.me/api/portraits/med/women/5.jpg"
        },
        {
            "id": "60d0fe4f5311236168a109d0",
            "title": "mr",
            "firstName": "Emre",
            "lastName": "Asikoglu",
            "picture": "https://randomuser.me/api/portraits/med/men/23.jpg"
        },
        {
            "id": "60d0fe4f5311236168a109d1",
            "title": "mr",
            "firstName": "Kent",
            "lastName": "Brewer",
            "picture": "https://randomuser.me/api/portraits/med/men/52.jpg"
        },
        {
            "id": "60d0fe4f5311236168a109d2",
            "title": "mr",
            "firstName": "Evan",
            "lastName": "Carlson",
            "picture": "https://randomuser.me/api/portraits/med/men/80.jpg"
        },
        {
            "id": "60d0fe4f5311236168a109d3",
            "title": "mr",
            "firstName": "Friedrich-Karl",
            "lastName": "Brand",
            "picture": "https://randomuser.me/api/portraits/med/men/7.jpg"
        }
    ],
    "total": 99,
    "page": 0,
    "limit": 10
}
"""

const val DETAILED_USER = """
{
    "id": "60d0fe4f5311236168a109ca",
    "title": "ms",
    "firstName": "Sara",
    "lastName": "Andersen",
    "picture": "https://randomuser.me/api/portraits/women/58.jpg",
    "gender": "female",
    "email": "sara.andersen@example.com",
    "dateOfBirth": "1996-04-30T19:26:49.610Z",
    "phone": "92694011",
    "location": {
        "street": "9614, Sandermarksvej",
        "city": "Kongsvinger",
        "state": "Nordjylland",
        "country": "Denmark",
        "timezone": "-9:00"
    },
    "registerDate": "2021-06-21T21:02:07.374Z",
    "updatedDate": "2021-06-21T21:02:07.374Z"
}
"""

class MockDummyApiServer(private val usedPort: Int = TEST_PORT) {
    companion object {
        const val TEST_PORT = 30080
    }

    private var webServer: MockWebServer? = null

    fun startMockWebServer() {
        webServer = MockWebServer().apply {
            dispatcher = object: Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return request.path?.run {
                        when {
                            startsWith("/user/malformed") -> MockResponse().setBody(DETAILED_USER.dropLast(10))
                            startsWith("/user?") -> MockResponse().setBody(USER_LIST)
                            startsWith("/user/") -> MockResponse().setBody(DETAILED_USER)
                            else -> MockResponse().setResponseCode(404)
                        }
                    } ?: MockResponse().setResponseCode(404)
                }
            }

            start(usedPort)
        }
    }

    fun stopMockWebServer() {
        webServer?.shutdown()
        webServer = null
    }
}