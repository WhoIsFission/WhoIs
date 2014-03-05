package com.e8security.cloudchamber.whois.test;
/**
 * 
 * This class is used for constructing Interval Tree which can hold range of IP Addresses
 * and provides mechanism of searching specific, generic and all ranges of IP address.
 * 
 */
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
public class CacheTest<T>{
	/**
	 * Static inner class is used for holding ranges 
	 *
	 * @param <T>
	 */
    public static class TreeNode<T> implements Comparable<TreeNode<T>>{
        private T low;
        private T high;
public T getLow(){
	return this.low;
}

public T getHigh(){
	return this.high;
}
		public TreeNode<T> left;
        public TreeNode<T> right;
        public T max;
        private int height;
        private TreeNode(T l, T h){
            this.low=l;
            this.high=h;
            this.max=high;
            this.height=1;
            this.left=null;// changes made
            this.right=null;//changes made
        }
        
        public int compareTo(TreeNode<T> nod){
        Comparable cLow=(Comparable) low;
        Comparable cHigh=(Comparable) high;
        Comparable nLow=(Comparable) nod.low;
        Comparable nHigh=(Comparable) nod.high; 
        if((cLow.compareTo(nLow)<0&&nHigh.compareTo(cHigh)<0)||(cLow.compareTo(nHigh))>0){
        		return 1;
        }
        
        if((cLow.compareTo(nLow)>0&&nHigh.compareTo(cHigh)>0)||(nLow.compareTo(cHigh))>0){
        		return -1;
        }
        
        return 0;
    }
    }
    public TreeNode<T> root;

/**
 * 
 * insert is used to insert range and helps to build tree 
 * which could be used for searching specific IP address.
 * 
 * @param l
 * @param h
 */
	public void insert(T l, T h){
        root=insert(root, l, h);
    }
	
	
	public int getHeight(){
	
		//System.out.println(TestIntervalTree.longToIp((Long)root.getLow()));
return root.height;		
	}

	/*
	 * To insert a specific range specified by low and high as nodes in a tree mentioned by node. 
	 *  
	 */
    private TreeNode<T> insert(TreeNode<T> node, T l, T h){
        if(node==null){
            return new TreeNode<T>(l, h);
        }
        else{
            int k=((Comparable)node.low).compareTo(l);
            if(k>0){
                node.left=insert(node.left, l, h);
            }
            else{
                node.right=insert(node.right, l, h);
            }
            node.height=Math.max(height(node.left), height(node.right))+1;
            node.max=findMax(node);
            int hd = heightDiff(node);
            if(hd<-1){
                int kk=heightDiff(node.right);
                if(kk>0){
                    node.right=rightRotate(node.right);
                    return leftRotate(node);
                }
                else{
                    return leftRotate(node);
                }
            }
            else if(hd>1){
                if(heightDiff(node.left)<0){
                    node.left = leftRotate(node.left);
                    return rightRotate(node);
                }
                else{
                    return rightRotate(node);
                } 
            }
            else;
        }
      
        return node;
    }
    
    /*
     * 
     * Left rotate the tree when height of right subtree is greater than 1
     */
    private TreeNode<T> leftRotate(TreeNode<T> n){
        TreeNode<T> r =  n.right;
        n.right = r.left;
        r.left=n;
        n.height=Math.max(height(n.left), height(n.right))+1;
        r.height=Math.max(height(r.left), height(r.right))+1;
        n.max=findMax(n);
        r.max=findMax(r);
        return r;
    }
    
    /*
     * Left rotate the tree when height of right subtree is greater than 1
     */
    
    private TreeNode<T> rightRotate(TreeNode<T> n){
        TreeNode<T> r =  n.left;
        n.left = r.right;
        r.right=n;
        n.height=Math.max(height(n.left), height(n.right))+1;
        r.height=Math.max(height(r.left), height(r.right))+1;
        n.max=findMax(n);
        r.max=findMax(r);
        return r;
    }
    
    /*
     * Compute the Height difference of left subtree- rightSubTree
     * 
     */
    private int heightDiff(TreeNode<T> a){
        if(a==null){
            return 0;
        }
        return height(a.left)-height(a.right);
    }
    
    /*
     * Computes the height of the tree.
     *
     */
    private int height(TreeNode<T> a){
        if(a==null){
            return 0;
        }
        return a.height;
    }
    
    
    /*
     * To compute the max value(including subtrees) or extreme right value in a range.
     * 
     */
    private T findMax(TreeNode<T> n){
    	
    	n.max=n.high;
        if(n.left==null && n.right==null){
            return n.max;
        }
        if(n.left==null){
            if(((Comparable)n.right.max).compareTo(n.max)>0){
                return n.right.max;
            }
            else{
                return n.max;
            }
        }
        if(n.right==null){
           if(((Comparable)n.left.max).compareTo(n.max)>0){
                return n.left.max;
            }
            else{
                return n.max;
            } 
        }
        Comparable c1 = (Comparable)n.left.max;
        Comparable c2 = (Comparable)n.right.max;
        Comparable c3 = (Comparable)n.max;
        T max=null;
        if(c1.compareTo(c2)<0){
            max=n.right.max;
        }
        else{
            max=n.left.max;
        }
        if(c3.compareTo((Comparable)max)>0){
            max=n.max;
        }
        return max;
    }

/**
 * 
 * Search specific Interval where given Key lies
 * 
 * @param keySearch
 * @return
 */
public TreeNode searchSpecificInterval(T keySearch){
	return searchSpecificIntervalHelper(root, keySearch);
}


/*
 * Helper method for searching specifiv interval 
 * 
 */
private TreeNode searchSpecificIntervalHelper(TreeNode<T> nod,T keySearch){
	TreeNode<T> left=null;
	TreeNode<T> right=null;
	TreeNode<T> curr=null;
	TreeNode<T> returnNode=null;
	if(nod==null)// changes 
		return null;
	if(nod.left!=null&&((Comparable)nod.left.max).compareTo(keySearch)>=0){
		left=searchSpecificIntervalHelper(nod.left,keySearch);
    }
   
	if(nod.right!=null&&((Comparable)nod.right.max).compareTo(keySearch)>=0){
		right=searchSpecificIntervalHelper(nod.right,keySearch);
     }
	if(isInside(nod, keySearch)){
		curr=nod;
	}	
	if(left==null&&right==null)
		return curr;
	
	if(left!=null&&(right==null||left.compareTo(right)<=0))
		returnNode=left;
	else
		returnNode=right;
	
	if(returnNode!=null&&(curr==null||returnNode.compareTo(curr)<=0))
		return returnNode;
	else
		return curr;

}

/**
 * 
 * Search Generic interval where given key lies
 * 
 * @param keySearch
 * @return
 */
public TreeNode searchGenericInterval(T keySearch){
	return searchGenericIntervalHelper(root, keySearch);
}

/*
 * 
 * Helper method for searching Generic interval
 */

private TreeNode searchGenericIntervalHelper(TreeNode<T> nod,T keySearch){
	TreeNode<T> left=null;
	TreeNode<T> right=null;
	TreeNode<T> curr=null;
	TreeNode<T> returnNode=null;
	if(nod==null)// changes
		return null;
	if(nod.left!=null&&((Comparable)nod.left.max).compareTo(keySearch)>=0){
		left=searchSpecificIntervalHelper(nod.left,keySearch);
    }
   
	if(nod.right!=null&&((Comparable)nod.right.max).compareTo(keySearch)>=0){
		right=searchSpecificIntervalHelper(nod.right,keySearch);
     }
	if(isInside(nod, keySearch)){
		curr=nod;
	}
	
	if(left==null&&right==null)
		return curr;
	if(left!=null&&(right==null||left.compareTo(right)>=0))
		returnNode=left;
	else
		returnNode=right;
	
	if(returnNode!=null&&(curr==null||returnNode.compareTo(curr)>=0))
		return returnNode;
	else
		return curr;

}

/**
 * Search all the intervals where given key lies
 * 
 * @param keySearch
 * @return
 */

public Set<TreeNode> setOfIntervalSearch(T keySearch){
	return intervalSearchForListHelper(root, keySearch);
}


/*
 * Search all the intervals where given lies.
 * 
 */

private Set<TreeNode> intervalSearchForListHelper(TreeNode<T> nod,T keySearch){
    if(nod==null)// changes
    	return Collections.EMPTY_SET;
	
    Set<TreeNode> setOfNodes=new TreeSet<TreeNode>();
    	if(isInside(nod, keySearch)){
    		setOfNodes.add(nod);
    	}
    	if(nod.left!=null&&((Comparable)nod.left.max).compareTo(keySearch)>=0){
    		setOfNodes.addAll(intervalSearchForListHelper(nod.left,keySearch));
        }
       
    	if(nod.right!=null&&((Comparable)nod.right.max).compareTo(keySearch)>=0){
    		setOfNodes.addAll(intervalSearchForListHelper(nod.right,keySearch));
         }
    return setOfNodes;
}

/*
 * verifies whether key(t) is inside the range mentioned.
 * 
 */

private boolean isInside(TreeNode<T> node, T t){

        Comparable cLow=(Comparable)node.low;
        Comparable cHigh=(Comparable)node.high;
        int i = cLow.compareTo(t);
        int j = cHigh.compareTo(t);
        if(i<=0 && j>=0){
            return true;
        }
        return false;
    }
	
/*
 * Helper method for deleting specific interval 
 * 
 */
private DeleteWhoisEntry<T> deleteSpecificInterval(TreeNode<T> nod,TreeNode<T> parent,T keySearch){
	if(nod==null)//changes
		return null;//changes
	DeleteWhoisEntry<T> left=null;
	DeleteWhoisEntry<T> right=null;
	DeleteWhoisEntry<T> curr=null;
	DeleteWhoisEntry<T> returnNode=null;
	if(nod.left!=null&&((Comparable)nod.left.max).compareTo(keySearch)>=0){
		left=deleteSpecificInterval(nod.left,nod,keySearch);
    }
   
	if(nod.right!=null&&((Comparable)nod.right.max).compareTo(keySearch)>=0){
		right=deleteSpecificInterval(nod.right,nod,keySearch);
     }
	if(isInside(nod, keySearch)){
		curr=new DeleteWhoisEntry<T>();
		curr.aNode=nod;
		curr.aParent=parent;
	}	
	if(left==null&&right==null)
		return curr;
	
	if(left!=null&&(right==null||(left).compareTo(right)<=0))
		returnNode=left;
	else
		returnNode=right;
	
	if(returnNode!=null&&(curr==null||returnNode.compareTo(curr)<=0))
		return returnNode;
	else
		return curr;

}

	
/**
 * Deleting specific IP range in a cache
 * @param keySearch
 */
public void deleteCacheEntry(T keySearch){
	DeleteWhoisEntry<T> delNode=deleteSpecificInterval(root,null, keySearch);
	if(delNode!=null)
	deleteCacheEntry(delNode.aNode, delNode.aParent);
}

/*
 * Private delete cache helper method
 * 
 */

private void deleteCacheEntry(TreeNode<T> node,TreeNode<T> parent){
	
		//TODO delete it
		if(node.left==null&&node.right==null){
			if(parent!=null){
			if(parent.left==node){
				parent.left=null;
			}else {
				parent.right=null;
				}
		}else{
			root=null;
		      }	
			}else if(node.left==null){
				if(parent!=null){
			if(parent.left==node){
				parent.left=node.right;
			}else 
				parent.right=node.right;
		}else{
			root=node.right;
		}	
				}else if(node.right==null){
		if(parent!=null){
			if(parent.left==node){
				parent.left=node.left;
			}else 
				parent.right=node.left;
			}else{
				root=node.left;
			}	
				}else if(node.left!=null&&node.right!=null){
				TreeNode<T> nodeToInsert=getSucessorNode(node,parent);
				if(parent!=null){
					if(parent.left==node){
					parent.left=nodeToInsert;
				}else 
					parent.right=nodeToInsert;
				}
				nodeToInsert.left=node.left;
				nodeToInsert.right=node.right;
				
				if(parent==null){
					root=nodeToInsert;
				}
				
			}
		root=balanceTree(root);
		node.left=null;//changes
		node.right=null;//changes
}

/*
 * Getting the successor node
 * 
 */
private TreeNode<T> getSucessorNode(TreeNode<T> node,TreeNode<T> parent){
	parent=node;
	node=node.right;
	boolean flag=true;
	while(node.left!=null){
		parent=node;
		node=node.left;
		flag=false;
	}
	if(!flag)
		parent.left=node.right;
	else
		parent.right=node.right;
return node;
}

/*
 * 
 * Balancing the tree
 * 
 */
private TreeNode<T> balanceTree(TreeNode<T> node){
	if(node==null)//changes done
		return null;// changes done

	node.left=balanceTree(node.left);
	node.right=balanceTree(node.right);

	node.height=Math.max(height(node.left), height(node.right))+1;
    node.max=findMax(node);
	int hd = heightDiff(node);
    if(hd<-1){
        int kk=heightDiff(node.right);
        if(kk>0){
            node.right=rightRotate(node.right);
            return leftRotate(node);
        }
        else{
            return leftRotate(node);
        }
    }
    else if(hd>1){
        if(heightDiff(node.left)<0){
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        else{
            return rightRotate(node);
        } 
    }
    else return node;
}

/**
 * 
 * Helper class for DeleteWhoisEntry
 * @author Abhijit
 *
 * @param <T>
 */
private class DeleteWhoisEntry<T> implements Comparable<DeleteWhoisEntry<T>>{
	TreeNode<T> aNode;
	TreeNode<T> aParent;

	 DeleteWhoisEntry(){
		 
	 }

	@Override
	public int compareTo(DeleteWhoisEntry<T> o) {
		// TODO Auto-generated method stub
		return this.aNode.compareTo(o.aNode);
	}
}
	
	
}