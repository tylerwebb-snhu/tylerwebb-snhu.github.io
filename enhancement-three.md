[Home](index.html) | [Code Review](code-review.html) | [Milestone Two](enhancement-one.html) | [Milestone Three](enhancement-two.html) | [Milestone Four](enhancement-three.html)

CS-499-12675-M01

Milestone Four: Enhancement Three: Databases

Tyler Webb

October 25, 2026

The artifact is the Item feature’s data layer in DoveInventory, the same Kotlin and Jetpack Compose mobile inventory app I originally built for CS 360: Mobile Architecture and Programming. This piece covers ItemDao, ItemRepository, and the use cases that sit between the database and the rest of the app, and it’s what I enhanced for the databases category.

I selected this artifact because it let me address a conflict with my Module One plan. That plan called for building out UserDatabase and UserDao from empty stubs into a working Room schema, but that work was already done in Milestone Two, since the architecture enhancement there needed a working persistence layer to route through. Resubmitting that same work here wouldn’t have shown any new progress, so I moved the databases enhancement to the Item feature instead. Before this enhancement, ItemDao.getItems() pulled every row from the item table with no WHERE clause at all, and all the search and category filtering added in Milestone Three happened in Kotlin after every row was already loaded into memory. I added a new parameterized query, searchItems, that filters by item name, reference number, and the five category flags, and sorts the results, directly in SQLite. I translated the app’s Set&lt;ItemFilter&gt; into individual Boolean parameters in ItemRepositoryImpl so the query logic stays entirely inside the data layer. I also rebuilt ItemListViewModel’s items pipeline around flatMapLatest, so a change to the search text or the active filters now triggers a fresh, filtered query instead of refiltering a static list that was loaded once. Because of this change, the \_items field, the ItemsState data class, and the getItems job were all deprecated, dead code. I removed all three rather than leave them behind.

In Module One I planned this category against Course Outcome 4 and Course Outcome 5, but that plan was written for the Auth database work that ended up happening in Milestone Two instead. For the enhancement I actually built, both outcomes are still applicable. Outcome 4 is met since writing a parameterized multi-condition query and restructuring the ViewModel around flatMapLatest is a well-founded, industry standard approach to handle search at the database layer instead of in application code. Outcome 5 is met since this is the first query in the app that takes free text user input directly into a SQL WHERE clause. I used Room’s bind parameters for the search text instead of concatenating it into the query string by hand. That is exactly the kind of structural decision that prevents a SQL injection vulnerability from being introduced in the first place. The update to my plan is that outcome coverage for the databases category shifted entirely from the Auth feature to the Item feature once I realized the original artifact was already finished.

The main challenge was translating the Kotlin filter logic exactly into SQL. The original matching logic said an item passes if no filters are active, or if it matches one selected filter, and I had to recreate that same OR logic across selected filters behavior as a SQL condition with one Boolean parameter per category instead of a Kotlin Set. I also had to decide what happened to the old getItems based path once the new query replaced it. Leaving \_items, ItemsState, and the old job in place would have left unused code behind, the same issue my instructor flagged on the Milestone One code review. I removed all three instead. The one trade-off I accepted rather than engineered around is that the list now takes about half a second to appear when the screen first opens, since debounce delays even the first emission, where before the list loaded instantly and only the filtering was delayed. I learned that extending a database layer sometimes means recognizing when a plan from an earlier module no longer matches reality, and adjusting instead of forcing the original plan to fit.

The only feedback I received during the milestones was on the Milestone One code review, where my instructor noted the code was missing comments. I added a comment above the searchItems query that explains how each category parameter works, along with comments in ItemRepositoryImpl and ItemListViewModel that explain the new query flow. Outcomes 4 and 5 are met. Outcome 3 is partially met since accepting the half second delay on first load was a deliberate design trade-off. Outcome 1 is partially met through the code comments. Outcome 2 is not met by this enhancement on its own. The code review video and the narratives cover it instead. I also did not add automated tests for the new query.

Original files

- [ItemDao.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/original/ItemDao.kt)
- [ItemListViewModel.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/original/ItemListViewModel.kt)
- [ItemModule.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/original/ItemModule.kt)
- [ItemRepository.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/original/ItemRepository.kt)
- [ItemRepositoryImpl.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/original/ItemRepositoryImpl.kt)
- [ItemsState.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/original/ItemsState.kt)
- [ItemUseCases.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/original/ItemUseCases.kt)

Enhanced files

- [ItemDao.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/enhanced/ItemDao.kt)
- [ItemListViewModel.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/enhanced/ItemListViewModel.kt)
- [ItemModule.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/enhanced/ItemModule.kt)
- [ItemRepository.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/enhanced/ItemRepository.kt)
- [ItemRepositoryImpl.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/enhanced/ItemRepositoryImpl.kt)
- [ItemUseCases.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/enhanced/ItemUseCases.kt)
- [SearchItemsUseCase.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement-three/enhanced/SearchItemsUseCase.kt)

[MilestoneFourNarrative_FinalProject_TylerWebb.docx](documents/MilestoneFourNarrative_FinalProject_TylerWebb.docx)
