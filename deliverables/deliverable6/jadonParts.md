# 4. Code quality

We refactored our MainVerticle to follow a consistent pattern for every GET/POST Request handler. We also adopted a
Manager pattern for the data objects we are dealing with, making every Database interaction we have follow the same
convention. We used the default formatter in IntelliJ to keep all the code looking similar.

# 5. Lessons learned

Our biggest problem was choosing technologies that were difficult to use. Using PostgreSQL and Docker turned out to be
challenging. A simpler language and smaller libraries would have made creating the website faster. We are still working
on being comfortable merging big git branches.
