package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {	
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		Stack<TagNode> newtags = new Stack<TagNode>();
		String a = sc.nextLine();
		root = new TagNode(a.substring(1, a.length()-1), null, null);
		newtags.push(root);
		String b = "";
		while (sc.hasNextLine())
		{
			b = sc.nextLine();
			if (b.charAt(0) == '<')
			{
				if (b.charAt(1) == '/')
				{
					newtags.pop();
				}
				else
				{ 
					TagNode temp = new TagNode(b.substring(1, b.length()-1), null, null);
					if (newtags.peek().firstChild == null)
					{
						newtags.peek().firstChild = temp;		
						newtags.push(temp);
					}
					else 
					{
						TagNode ptr = newtags.peek().firstChild;
						while (ptr.sibling != null)
						{
							ptr = ptr.sibling;
						}
						ptr.sibling = temp;
						newtags.push(temp);
					}
				}
			}
			else 
			{
				TagNode c = new TagNode(b, null, null);
				if (newtags.peek().firstChild == null)
				{
					newtags.peek().firstChild = c;
				}
				else 
				{
					TagNode ptr = newtags.peek().firstChild;
					while (ptr.sibling != null)
					{
						ptr = ptr.sibling;
					}
					ptr.sibling = c;
				}
			}
		}	
	}
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) 
	{
		/** COMPLETE THIS METHOD **/
		if ((oldTag.equals("p") || oldTag.equals("b") || oldTag.equals("em"))&& (newTag.equals("p") || newTag.equals("b") || newTag.equals("em")))
		{
			replaceRecursive(oldTag, newTag, root);
		}
		else if ((oldTag.equals("ol") || oldTag.equals("ul")) && (newTag.equals("ol") || newTag.equals("ul")))
		{
			replaceRecursive(oldTag, newTag, root);
		}
		else 
		{
			return;
		}
	}
	
	private static void replaceRecursive(String OTag, String NTag, TagNode NRoot)
	{
		if (NRoot == null)
		{
			return;
		}
		if (NRoot.tag.equals(OTag) && NRoot.firstChild != null){
			NRoot.tag = NTag;
		}
		replaceRecursive(OTag, NTag, NRoot.firstChild);
		replaceRecursive(OTag, NTag, NRoot.sibling);
		return;
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) 
	{
		/** COMPLETE THIS METHOD **/
		boldRowRecursive(row, root);
		
		//printTree(root);
	}
	
	/*private static void printTree(TagNode root){
		if (root == null){
			return;
		}
		System.out.println(root.tag);
		printTree(root.firstChild);
		printTree(root.sibling);
	}*/
	
	private void boldRowRecursive(int NRow, TagNode NRoot)
	{
		if (NRoot == null)
		{
			return;
		}
		if (NRoot.tag.equals("table"))
		{
			TagNode ptr = NRoot.firstChild;
			for (int i=1; i<NRow; i++)
			{
				ptr = ptr.sibling;
			}
			TagNode teed = ptr.firstChild;
			TagNode bold = new TagNode("b", teed.firstChild, null);
			teed.firstChild = bold;
			while (teed.sibling != null)
			{
				teed = teed.sibling;
				bold = new TagNode("b", teed.firstChild, null);
				teed.firstChild = bold;
			}
		}
		else 
		{ 
			boldRowRecursive(NRow, NRoot.firstChild);
			boldRowRecursive(NRow, NRoot.sibling);
		}
		return;
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) 
	{
		/** COMPLETE THIS METHOD **/
		removeTagHelper(tag, root);
		if (tag.equals("ol") || tag.equals("ul"))
		{
			recurcall(root);
		}
		//printTree(root);
	}
	
	private static void recurcall(TagNode NRoot)
	{
		if (NRoot.firstChild == null || NRoot == null)
		{
			return;
		}
		if (!(NRoot.tag.equals("ol") || NRoot.tag.equals("ul")) && NRoot.firstChild.tag.equals("li") )
		{
			NRoot.firstChild.tag = "p";
		}
		else 
		{
			recurcall(NRoot.firstChild);
			recurcall(NRoot.sibling);
		}
	}
	
	private static void removeTagHelper(String NTag, TagNode NRoot)
	{
		if (NRoot == null || (NRoot.firstChild == null && NRoot.sibling == null))
		{
			return;
		}
		if ((NRoot.firstChild != null) && (NRoot.firstChild.firstChild != null) && NRoot.firstChild.tag.equals(NTag))
		{
			if (NTag.equals("ol") || NTag.equals("ul"))
			{
				if (NRoot.firstChild.firstChild.sibling != null)
				{
					TagNode ptr = NRoot.firstChild.firstChild.sibling;
					if (ptr.tag.equals("li"))
					{
						ptr.tag = "p";
					}
					while (ptr.sibling != null) 
					{
						ptr = ptr.sibling;
						if (ptr.tag.equals("li"))
						{
							ptr.tag = "p";
						}
					}
					ptr.sibling = NRoot.firstChild.sibling;
					TagNode temp = NRoot.firstChild.firstChild;
					if (temp.tag == "li")
					{
						temp.tag = "p";
					}
					NRoot.firstChild = temp;
				}
				else 
				{
					TagNode temp = NRoot.firstChild.firstChild;
					if (temp.tag.equals("li"))
					{
						temp.tag = "p";		
					}
					temp.sibling = NRoot.firstChild.sibling;
					NRoot.firstChild = temp;
				}	
				removeTagHelper(NTag, NRoot);
			}			
			if (NTag.equals("p") || NTag.equals("em") || NTag.equals("b"))
			{
				if (NRoot.firstChild.firstChild.sibling != null) 
				{
					TagNode ptr = NRoot.firstChild.firstChild.sibling;
					while (ptr.sibling != null) 
					{
						ptr = ptr.sibling;
					}
					ptr.sibling = NRoot.firstChild.sibling;
					TagNode temp = NRoot.firstChild.firstChild;
					NRoot.firstChild = temp;
				}
				else 
				{
					TagNode temp = NRoot.firstChild.firstChild;
					temp.sibling = NRoot.firstChild.sibling;
					NRoot.firstChild = temp;
				}
				
				removeTagHelper(NTag, NRoot);
			}
		}
		if ((NRoot.sibling != null) && NRoot.sibling.tag.equals(NTag)) 
		{ 
			if (NTag.equals("ol") || NTag.equals("ul"))
			{
				if (NRoot.sibling.firstChild.sibling != null)
				{
					TagNode ptr =NRoot.sibling.firstChild.sibling;
					if (ptr.tag.equals("li"))
					{
						ptr.tag = "p";
					}
					while (ptr.sibling != null) 
					{
						ptr = ptr.sibling;
						if (ptr.tag.equals("li"))
						{
							ptr.tag = "p";
						}
					}
					ptr.sibling = NRoot.sibling.sibling;
					if (NRoot.sibling.firstChild.tag.equals("li")) 
					{
						NRoot.sibling.firstChild.tag = "p";
					}
					TagNode temp = NRoot.sibling.firstChild;
					if (temp.tag == "li")
					{
						temp.tag = "p";
					}
					NRoot.sibling = temp;
				}
				else 
				{
					TagNode temp = NRoot.sibling.firstChild;
					temp.sibling = NRoot.firstChild.sibling;
					NRoot.sibling = temp;
				}
				
				removeTagHelper(NTag, NRoot);
			}
			
			if (NTag.equals("p") || NTag.equals("em") || NTag.equals("b"))
			{
				if (NRoot.sibling.firstChild.sibling != null) 
				{
					TagNode ptr = NRoot.sibling.firstChild.sibling;
					while (ptr.sibling != null) 
					{
						ptr = ptr.sibling;
					}
					ptr.sibling = NRoot.sibling.sibling;
					TagNode tm1 = NRoot.sibling.firstChild;
					NRoot.sibling = tm1;
				}
				else 
				{
					TagNode tm2 = NRoot.sibling.firstChild;
					tm2.sibling = NRoot.sibling.sibling;
					NRoot.sibling = tm2;
				}
				removeTagHelper(NTag, NRoot);
			}
		}
		else 
		{
			removeTagHelper(NTag, NRoot.firstChild);
			removeTagHelper(NTag, NRoot.sibling);
		}
		return;
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) 
	{
		/** COMPLETE THIS METHOD **/
		if (tag.equals("em") || tag.equals("b"))
		{
			addTagHelper(word.toLowerCase(),tag,root);
		}
		return;
	}
	
	private static void addTagHelper(String NWord, String NTag, TagNode NRoot)
	{
		if (NRoot == null || (NRoot.firstChild == null && NRoot.sibling == null))
		{
			return;
		}
		
		else if (NRoot.firstChild != null && NRoot.firstChild.firstChild == null && NRoot.firstChild.tag.toLowerCase().contains(NWord))
		{
			if (NRoot.firstChild.tag.equalsIgnoreCase(NWord)) 
			{
				TagNode theTag = new TagNode(NTag, NRoot.firstChild, NRoot.firstChild.sibling);
				NRoot.firstChild = theTag;
			}
			else 
			{
				int index = NRoot.firstChild.tag.indexOf(NWord);
				if (index < 0)
				{
					return;
				}
				else 
				{
					int last = NWord.length();
					while (NRoot.firstChild.tag.charAt(index + last) == '!' || NRoot.firstChild.tag.charAt(index + last) == '.' 
							|| NRoot.firstChild.tag.charAt(index + last) == ',' || NRoot.firstChild.tag.charAt(index + last) == '?' || 
							NRoot.firstChild.tag.charAt(index + last) == ';' || NRoot.firstChild.tag.charAt(index + last) == ':'
							)
					{
						last++;
					}					
					
					if (index == 0)
					{
						TagNode L = new TagNode(NRoot.firstChild.tag.substring(0, last), NRoot.firstChild.firstChild, null);
						TagNode R = new TagNode(NRoot.firstChild.tag.substring(last), null, NRoot.firstChild.sibling);
						TagNode T = new TagNode(NTag, L, R);
						NRoot.firstChild = T;
						
					}
					else if (index > 0 && index+last < NRoot.firstChild.tag.length() - 1)
					{
						TagNode F = new TagNode(NRoot.firstChild.tag.substring(0, index), null, null);
						TagNode S = new TagNode(NRoot.firstChild.tag.substring(index, index+last), null, null); 
						TagNode T = new TagNode(NRoot.firstChild.tag.substring(index+last), null, NRoot.firstChild.sibling);
						TagNode Tag = new TagNode(NTag, S, T);
						F.sibling = Tag;
						NRoot.firstChild = Tag;
					}
					else 
					{
						if (NRoot.firstChild.tag.length() == index+last)
						{
							TagNode FH = new TagNode(NRoot.firstChild.tag.substring(0, index), null, null);
							TagNode SH = new TagNode(NRoot.firstChild.tag.substring(index), null, null); 
							TagNode Tag = new TagNode(NTag, SH, NRoot.firstChild.sibling);
							FH.sibling = Tag;
							NRoot.firstChild = Tag;
						}
					}
				}
			}
		}
		
		else if (NRoot.sibling != null && NRoot.sibling.firstChild == null && NRoot.sibling.tag.toLowerCase().equalsIgnoreCase(NWord))
		{
			if (NRoot.sibling.tag.equalsIgnoreCase(NWord)) 
			{ 
				TagNode theTag = new TagNode(NTag, NRoot.sibling, NRoot.sibling.sibling);
				NRoot.sibling = theTag;
			}
			else 
			{
				int index1 = NRoot.sibling.tag.indexOf(NWord);
				if (index1 < 0)
				{
					return;
				}
				else 
				{
					int last = NWord.length();
					while (NRoot.sibling.tag.charAt(index1 + last) == '!' || NRoot.sibling.tag.charAt(index1 + last) == '.' 
							|| NRoot.sibling.tag.charAt(index1 + last) == ',' || NRoot.sibling.tag.charAt(index1 + last) == '?' || 
							NRoot.sibling.tag.charAt(index1 + last) == ';' || NRoot.sibling.tag.charAt(index1 + last) == ':'
							)
					{
						last++;
					}					
					if (index1 == 0)
					{
						TagNode L = new TagNode(NRoot.sibling.tag.substring(0, last), NRoot.sibling.firstChild, null);
						TagNode R = new TagNode(NRoot.sibling.tag.substring(last), null, NRoot.sibling.sibling);
						TagNode T = new TagNode(NTag, L, R);
						NRoot.sibling = T;
					}
					else if (index1 > 0 && index1+last < NRoot.sibling.tag.length() - 1)
					{
						TagNode F = new TagNode(NRoot.sibling.tag.substring(0, index1), null, null);
						TagNode S = new TagNode(NRoot.sibling.tag.substring(index1, index1+last), null, null);
						TagNode T = new TagNode(NRoot.sibling.tag.substring(index1+last), null, NRoot.sibling.sibling);
						TagNode Tag = new TagNode(NTag, S, T);
						F.sibling = Tag;
						NRoot.sibling = Tag;
					}
					else 
					{
						if (NRoot.sibling.tag.length() == index1+last)
						{
							TagNode F = new TagNode(NRoot.sibling.tag.substring(0, index1), null, null);
							TagNode S = new TagNode(NRoot.sibling.tag.substring(index1), null, null);
							TagNode Tag = new TagNode(NTag, S , NRoot.sibling.sibling);
							F.sibling = Tag;
							NRoot.sibling = Tag;
						}
					}
				}
			}
		}
		
		else
		{
			addTagHelper(NWord, NTag, NRoot.firstChild);
			addTagHelper(NWord, NTag, NRoot.sibling);
		}
		return;
	}

			
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() 
	{
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
}

//Thank you