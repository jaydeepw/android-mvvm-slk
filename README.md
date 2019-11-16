# android-code-exercise
Skeleton project for the coding exercise for Android candidates

# Constraints
- if the search term matches or starts with anything found in the blacklist, no API call is performed
- if a search term is not found in the blacklist, there is an API call to lookup matches
- if the API returns no results, the search term is added to the blacklist

# Improvements
- MVP could be changed to use MVVM and android architecture components to make app more concise and maintainable
- Progress indicator can be shown while the API call is in progress

# To be noted
- The app caches all the blacklisted search queries in memory soon as it loads.
- `blacklist.txt` resides in the apk, so it is difficult to write it back. Hence it is being copied over initially to the internal
storage of the app in apps private `files` storage by a service called `BlackListCopyService` in background.
- There is one important consideration when updating the blacklist. While multiple queries are in-flight, when the result returns, the API
response does not tell which result is for which query. In such event, a query may get blacklisted if the data for some other query was empty. This
cannot be handled by the client until some changes are made on the backend.

# Libraries used
- `Glide` for image loading and caching