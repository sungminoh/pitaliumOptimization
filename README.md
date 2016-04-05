# Pitalium Optimization

### Reduce DiffPoints Union Time.
2016.04.05. revised.
Tree structure has critical problem.
just using the same MarkerGroup class and amended some in ImageUtil.java
While constructing MarkerGroups list, stack all the MarkerGroup which has intersection with the new MarkerGroup.
Only after this, union all the MarkerGroups in the stack.
This assures that no cross-wise iteration.

2016.04.04.
Using tree structure with x coordinate instead of 1:1 comparison.
Check whether each node is close to new node or not throgh the way of going down.
Using hashset structure to store closed pointes group(this will be MarkerGroup).
Using hashmap structure to store minimum and maximum of x, y coordinate.
