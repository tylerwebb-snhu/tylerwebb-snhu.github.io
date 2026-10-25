[Home](index.html) | [Code Review](code-review.html) | [Milestone Two](enhancement-one.html) | [Milestone Three](enhancement-two.html) | [Milestone Four](enhancement-three.html)

CS-499-12675-M01

Milestone Three

Tyler Webb

October 25, 2026

The artifact is the Item feature of DoveInventory, the same Kotlin and Jetpack Compose mobile inventory app I originally built for CS 360: Mobile Architecture and Programming. Item handles the app’s inventory list, and it is the piece I chose t enhance for the algorithms and data structure category.

I selected this artifact because it already had real search logic in place, but that logic only used a fraction of the data available to it. Item.doesMatchSearchQuery() checked the item name and nothing else, and the Item entity carried five boolean category flags, legend room, general warehouse, vehicle, dirty, and broken that existed on every row but were never referenced anywhere in the app. The enhancement expanded the matching logic into a real multi-criteria filter. I added an ItemFilter enum to represent the five categories, updated doesMatchSearchQuery to check the item’s reference number alongside its name and added a separate matchesFilter function so the category logic didn’t turn into on long if/else chain inside the main matching function. In ItemListViewModel, I added a StateFlow to track the active filters and combined it into the existing debounced search pipeline. That pipeline went from combining two flows, search text and items, to combining three. I also added a row of filter chips to ItemListScreen so a user can select categories, since there was no way to reach this logic from the UI before. This shows the ability to take an existing algorithm and extend it to use data that was already in the model but unused, without complicating the function with extra conditions.

In Module One I planned this enhancement against Course Outcome 3 and Course Outcome 4. Outcome 3 is met since the matching logic now evaluates multiple criteria, text and category, together. Keeping the filter check and the text check as two independent Booleans combine with an AND, instead of one long condition, was a deliberate design choice. Outcome 4 is met since the enhancement uses the same reactive Flow pattern the rest of the app already relies on. It combines state through StateFlow instead of manually tracking filtered results in a mutable list. There was no way to actually select a category filter anywhere in the app before this enhancement, so making it demonstrable meant adding the filter chip row to ItemListScreen as well like what was done with the Auth screen in Milestone Two.

The main challenge was combining a third flow into a pipeline that already combined two. The existing code combined search text with the item list directly. I had to combine the text flow with the filters flow first, then combine that result with the items flow. I also had to decide whether filter chip taps should go through the same 500 millisecond debounce as the search text, and decided against it, since debounce exists to avoid refiltering on every keystroke while someone is still typing, and a chip tap is a single action with nothing to wait out. I learned that extending an existing algorithm often means understanding why the current structure works the way it does before changing it, rather than just adding a new parameter and hoping it works.

The only feedback I received during the milestones was on the Milestone One code review, where the instructor noted the code was missing comments. I added comments to this enhancement that explain the design choices. For example, Item.kt explains why matchesFilter is kept separate from doesMatchSearchQuery, and ItemListViewModel explains what the active filter set tracks. Outcomes 3 and 4 are met. Outcome 1 is partially met through the code comments. Outcome 5 is not met since this enhancement does not change anything related to security. Outcome 2 is not met by this enhancement on its own. The code review video and the narratives cover it instead. I also did not add automated tests for the new matching logic.

Original files

- [Item.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_two/original/Item.kt)
- [ItemListScreen.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_two/original/ItemListScreen.kt)
- [ItemListViewModel.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_two/original/ItemListViewModel.kt)
- [strings.xml](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_two/original/strings.xml)

Enhanced files

- [Item.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_two/enhanced/Item.kt)
- [ItemListScreen.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_two/enhanced/ItemListScreen.kt)
- [ItemListViewModel.kt](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_two/enhanced/ItemListViewModel.kt)
- [strings.xml](https://github.com/tylerwebb-snhu/tylerwebb-snhu.github.io/blob/main/artifacts/enhancement_two/enhanced/strings.xml)

[MilestoneThreeNarative_FinalProject_TylerWebb.docx](documents/MilestoneThreeNarative_FinalProject_TylerWebb.docx)
