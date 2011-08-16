package org.eclipse.jetty.toolchain.version.issues;

import org.apache.commons.lang3.text.WordUtils;

/**
 * A Generic Issue
 */
public class Issue
{
    private String id;
    private String text;
    private int wrapColumn = 76;

    public Issue(String id, String subject)
    {
        this.id = id;
        this.text = subject;
    }

    public void appendText(String line)
    {
        line = line.trim();
        // Handle Bullet Points differently.
        if (line.charAt(0) == '*')
        {
            text += "\n   " + line;
        }
        else
        {
            // Append wrapped line
            text += " " + line;
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Issue other = (Issue)obj;
        if (id == null)
        {
            if (other.id != null)
            {
                return false;
            }
        }
        else if (!id.equals(other.id))
        {
            return false;
        }
        return true;
    }

    public String getId()
    {
        return id;
    }

    public String getText()
    {
        return text;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null)?0:id.hashCode());
        return result;
    }

    public boolean hasId()
    {
        return !id.startsWith("zz-");
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public String toString()
    {
        StringBuilder line = new StringBuilder();
        line.append(" + ");
        if (hasId())
        {
            line.append(WordUtils.wrap(id + " " + text,wrapColumn,"\n   ",false));
        }
        else
        {
            line.append(WordUtils.wrap(text,wrapColumn,"\n   ",false));
        }
        return line.toString();
    }
}
