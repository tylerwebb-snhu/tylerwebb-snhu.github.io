[Home](index.html) | [Code Review](code-review.html) | [Milestone Two](enhancement-one.html) | [Milestone Three](enhancement-two.html) | [Milestone Four](enhancement-three.html)

CS-499-12675-M01

Milestone Two

Tyler Webb

October 25, 2026

The artifact is the Auth feature of DoveInventory, a Kotlin and Jetpack Compose mobile inventory tracking application I originally built for CS 360: Mobile Architecture and Programming. Auth handles user login, registration, and session state for the app and it is the piece I chose to enhance for the software design and engineering category.

I selected this artifact because it showed a clear gap against the rest of the app. Every other feature in DoveInventory, like Item and Inventory, follows a repository and use case pattern that separates the data source from the ViewModel, but Auth skipped the pattern entirely. AuthViewModel called FirebaseAuth directly, and the registration flow was an empty TODO that never actually created an account. The enhancement brought Auth in line with the rest of the codebase. I added a User domain model, a UserRepository interface, and RegisterUserUseCase and GetUserIDUserCase classes, the same structure Item already uses. I built out UserDao and UserDatabase, which existed only as empty stubs before this, into a real Room schema, and wrote UserRepositoryImpl to bridge Firebase Auth for the credential and Room for the local profile record. I finished RegisterViewModel’s Register handler so it actually validates the input and calls the new use case, and I wired AuthModule’s Hilt providers the same way ItemModule already provides its own dependencies. This shows the same architectural consistency and validation logic the rest of the app already demonstrates, now applied to Auth.

In Module One, I planned this enhancement against Course Outcome 3 and Course Outcome 4. Outcome 3 is met since Auth now follows the same repository and user case pattern used elsewhere, matching the design and trade-off decisions already established in the codebase rather than introducing a new pattern. One deliberate trade-off was keeping the raw password inside Firebase only and never persisting it in Room, since the local use case did not need it. Outcome 4 is met since this enhancement uses Room, Hilt, and Kotlin coroutines the same way the rest of the app already does. One update to the plan is that RegisterScreen itself was never connected to any ViewModel before this enhancement since its fields and submit button were hardcoded and did nothing, so making the enhancement actually demonstrable meant wiring the screen as in addition to the ViewModel and data layer the original plan described.

The biggest challenge was the Firebase’s authentication API is callback based, but the rest of the app’s data layer uses Kotlin coroutines. I did not want to add a new dependency just to bridge that gap, so I wrapped the Firebase call in suspendCancellableCoroutine instead of pulling in a library like kotlinx-coroutines-play-services. That let UserRepostoryImpl expose a normal suspend function to the use case layer without changing anything about how Firebase itself works. I also learned how much the repository pattern isolates decisions like this. The ViewModel and use case never need to know whether Auth is backed by Firebase, Room, or both since they just call a suspend function and handle the result. The other challenge was realizing the UI was not connected to anything, which meant the scope of the enhancement was larger than the architecture only plan from Module One.

The only feedback I received during the milestones was on the Milestone One code review, where my instructor noted the code was missing comments. I added comments to this enhancement that explain the reasoning behind decisions that are not obvious from the code alone. For example, User.kt explains why the primary key is the Firebase UID and why there is no password field, and UserRepositoryImpl explains why the Firebase callback is wrapped in a coroutine. Outcomes 3 and 4 are met. Outcome 5 is partially met since RegisterUserUseCase validates every field before an account is created and the password is never stored locally. Outcome 1 is partially met through the in-code comments. Outcome 2 is not met by this enhancement on its own. The code review video and the narratives cover it instead. I also did not add automated tests, so the testing part of Outcome 4 is not shown here.

Original files

- [AuthModule.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/original/AuthModule.kt)
- [RegisterScreen.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/original/RegisterScreen.kt)
- [RegisterViewModel.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/original/RegisterViewModel.kt)
- [UserDao.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/original/UserDao.kt)
- [UserDatabase.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/original/UserDatabase.kt)

Enhanced files

- [AuthModule.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/AuthModule.kt)
- [GetUserIDUseCase.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/GetUserIDUseCase.kt)
- [RegisterScreen.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/RegisterScreen.kt)
- [RegisterUserUseCase.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/RegisterUserUseCase.kt)
- [RegisterViewModel.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/RegisterViewModel.kt)
- [User.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/User.kt)
- [UserDao.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/UserDao.kt)
- [UserDatabase.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/UserDatabase.kt)
- [UserRepository.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/UserRepository.kt)
- [UserRepositoryImpl.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/UserRepositoryImpl.kt)
- [UserUseCases.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_one/enhanced/UserUseCases.kt)

[MilestoneTwoNarrative_FinalProject_TylerWebb.docx](documents/MilestoneTwoNarrative_FinalProject_TylerWebb.docx)
